#!/usr/bin/env python3
"""End-to-end check of model downloads (docs/MODEL_ARTIFACT_ACCESS_PLAN.md, "Verification of the
whole feature", steps 1-5) against a running stack (docker compose --profile full).

What it does:
1. Trains the iris pipeline from docs/MODEL_PUBLISHING_GUIDE.md section 2 and converts it to ONNX
   with the snippet from section 3 ("Also offer an ONNX version"), checking ONNX Runtime agrees.
2. Logs it to MLflow with extra_files=["model.onnx"], and a second model with the tag
   downloadable=false, both in a new experiment (the catalog name).
3. Waits for the catalog sync, then checks the attachments (mlflow-model and onnx, href with
   ?format=, sizes), downloads both formats and compares predictions with the trained pipeline:
   the MLflow zip with mlflow.pyfunc, the ONNX zip with ONNX Runtime. Optionally (--clean-python)
   it also installs the zip's requirements.txt into a separate, empty virtual environment and loads
   the model there.
4. Negative cases: no token (401), a user without ROLE_USER (403; needs the Keycloak admin
   account, skipped otherwise), downloadable=false (404 naming it), an unknown format (404), an
   invalid format (400), and a POSTed specification pointing outside the allowed storage (404).

Requirements (client side): mlflow, scikit-learn, pandas, numpy, requests, skl2onnx, onnxruntime.

Configuration (environment variables; defaults match docker-compose.yml):
  TMF915_API_URL          http://localhost:13082/tmf-api/AiM/v4
  KEYCLOAK_URL            http://localhost:8080/auth
  KEYCLOAK_REALM          openslice
  KEYCLOAK_CLIENT_ID      osapiWebClientId
  TMF915_USERNAME / TMF915_PASSWORD        admin / openslice
  KEYCLOAK_ADMIN_USER / KEYCLOAK_ADMIN_PASSWORD   kcadmin / kcadmin (set KEYCLOAK_ADMIN_USER= to skip the 403 check)
  MLFLOW_TRACKING_URI     http://localhost:5000
  MLFLOW_S3_ENDPOINT_URL  http://localhost:9010
  AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY   minioadmin / minioadmin

Usage:
  python3 scripts/verify_model_artifacts.py [--clean-python /path/to/empty-venv/bin/python]
                                            [--wait 240] [--keep]

The script creates an experiment named artifact-verify-<timestamp> in MLflow and, with the 403
check, a Keycloak user named artifact-verify-<timestamp>. Run it against a throwaway stack.
Exit code 0 if every check passed.
"""

from __future__ import annotations

import argparse
import atexit
import io
import json
import os
import re
import shutil
import subprocess
import sys
import tempfile
import time
import zipfile

import requests

API = os.environ.get("TMF915_API_URL", "http://localhost:13082/tmf-api/AiM/v4").rstrip("/")
KC = os.environ.get("KEYCLOAK_URL", "http://localhost:8080/auth").rstrip("/")
REALM = os.environ.get("KEYCLOAK_REALM", "openslice")
CLIENT_ID = os.environ.get("KEYCLOAK_CLIENT_ID", "osapiWebClientId")
USERNAME = os.environ.get("TMF915_USERNAME", "admin")
PASSWORD = os.environ.get("TMF915_PASSWORD", "openslice")
KC_ADMIN = os.environ.get("KEYCLOAK_ADMIN_USER", "kcadmin")
KC_ADMIN_PASSWORD = os.environ.get("KEYCLOAK_ADMIN_PASSWORD", "kcadmin")

os.environ.setdefault("MLFLOW_TRACKING_URI", "http://localhost:5000")
os.environ.setdefault("MLFLOW_S3_ENDPOINT_URL", "http://localhost:9010")
os.environ.setdefault("AWS_ACCESS_KEY_ID", "minioadmin")
os.environ.setdefault("AWS_SECRET_ACCESS_KEY", "minioadmin")

# Run by --clean-python in the empty environment: load the model, print predictions as JSON.
CLEAN_LOADER = """
import json, sys
import pandas as pd
import mlflow
model = mlflow.pyfunc.load_model(sys.argv[1])
frame = pd.read_json(sys.argv[2], orient="split")
print(json.dumps([int(v) for v in model.predict(frame)]))
"""

results: list[tuple[bool, str]] = []


def check(ok: bool, what: str, detail: str = "") -> bool:
    results.append((bool(ok), what))
    print(f"  [{'PASS' if ok else 'FAIL'}] {what}" + (f": {detail}" if detail and not ok else ""))
    return bool(ok)


class Token:
    """A password-grant token, renewed before it expires (Keycloak's default lifespan is minutes)."""

    def __init__(self, username: str, password: str):
        self.username, self.password = username, password
        self.value, self.expires = "", 0.0

    def __str__(self) -> str:
        if time.time() > self.expires - 30:
            r = requests.post(f"{KC}/realms/{REALM}/protocol/openid-connect/token", timeout=30, data={
                "grant_type": "password", "client_id": CLIENT_ID,
                "username": self.username, "password": self.password})
            r.raise_for_status()
            body = r.json()
            self.value, self.expires = body["access_token"], time.time() + body["expires_in"]
        return self.value

    def headers(self) -> dict[str, str]:
        return {"Authorization": f"Bearer {self}"}


def train_and_log(experiment: str, workdir: str):
    """Guide section 2 and 3, as published; returns the pipeline, test data and the two model ids."""
    import inspect
    import mlflow
    import numpy as np
    from mlflow.models import infer_signature
    from sklearn.datasets import load_iris
    from sklearn.ensemble import RandomForestClassifier
    from sklearn.metrics import accuracy_score
    from sklearn.model_selection import train_test_split
    from sklearn.pipeline import Pipeline
    from sklearn.preprocessing import MinMaxScaler

    mlflow.set_experiment(experiment)
    iris = load_iris(as_frame=True)
    X_train, X_test, y_train, y_test = train_test_split(iris.data, iris.target, test_size=0.2, random_state=42)
    hp = {"n_estimators": 100, "max_depth": 8, "random_state": 42}
    pipeline = Pipeline([("scaler", MinMaxScaler()), ("clf", RandomForestClassifier(**hp))])
    pipeline.fit(X_train, y_train)
    predictions = pipeline.predict(X_test)

    # The guide's ONNX snippet (section 3), run in workdir because extra_files is a relative path.
    cwd = os.getcwd()
    os.chdir(workdir)
    try:
        from skl2onnx import to_onnx
        onx = to_onnx(pipeline, X_train.to_numpy(dtype=np.float32)[:1])
        with open("model.onnx", "wb") as f:
            f.write(onx.SerializeToString())
        import onnxruntime as ort
        session = ort.InferenceSession("model.onnx")
        onnx_labels = session.run(None, {session.get_inputs()[0].name: X_test.to_numpy(dtype=np.float32)})[0]
        check((onnx_labels == pipeline.predict(X_test)).all(), "guide snippet: ONNX Runtime predicts like the pipeline")

        common = dict(name="model", signature=infer_signature(X_test, predictions), input_example=X_test.head(2))
        if "skops_trusted_types" in inspect.signature(mlflow.sklearn.log_model).parameters:
            common["skops_trusted_types"] = ["sklearn.tree._tree.Tree"]
        ids = {}
        for kind, extra in (("onnx", {"extra_files": ["model.onnx"]}), ("optout", {"tags": {"downloadable": "false"}})):
            with mlflow.start_run():
                info = mlflow.sklearn.log_model(
                    pipeline, params={**hp, "config.version": f"artifact verification ({kind})"}, **common, **extra)
                mlflow.log_metric("test_accuracy", accuracy_score(y_test, predictions), model_id=info.model_id)
                ids[kind] = info.model_id
    finally:
        os.chdir(cwd)
    print(f"  logged {ids['onnx']} (with model.onnx) and {ids['optout']} (downloadable=false) in '{experiment}'")
    return pipeline, X_test, predictions, ids


def find_specs(token: Token, experiment: str, versions: set[str], wait: int) -> dict[str, dict]:
    """Pages through the catalog until both versions appear, or the wait runs out."""
    deadline = time.time() + wait
    while True:
        found, offset, limit = {}, 0, 100
        while True:
            r = requests.get(f"{API}/aiModelSpecification", headers=token.headers(), timeout=30,
                             params={"fields": "name,version,attachment,specCharacteristic",
                                     "offset": offset, "limit": limit})
            r.raise_for_status()
            page = r.json()
            found.update({s["version"]: s for s in page if s.get("name") == experiment and s.get("version") in versions})
            if len(page) < limit:
                break
            offset += limit
        if set(found) == versions or time.time() > deadline:
            return found
        time.sleep(10)


def download(token: Token | None, spec_id: str, fmt: str | None = None, raw_query: str = "") -> requests.Response:
    url = f"{API}/aiModelSpecification/{spec_id}/artifacts" + raw_query
    return requests.get(url, headers=token.headers() if token else {}, timeout=300,
                        params={"format": fmt} if fmt else None)


def exact_bytes(attachment: dict) -> int:
    m = re.search(r"(\d+) bytes\)", attachment.get("description", ""))
    return int(m.group(1)) if m else -1


def check_zip(r: requests.Response, attachment: dict, label: str, target: str) -> zipfile.ZipFile | None:
    if not check(r.status_code == 200, f"{label}: download is 200", f"{r.status_code} {r.text[:200]}"):
        return None
    check(r.headers.get("Content-Type", "").startswith("application/zip"), f"{label}: Content-Type is a zip",
          r.headers.get("Content-Type", ""))
    check("attachment;" in r.headers.get("Content-Disposition", ""), f"{label}: Content-Disposition attachment",
          r.headers.get("Content-Disposition", ""))
    z = zipfile.ZipFile(io.BytesIO(r.content))
    check(z.testzip() is None, f"{label}: zip is intact")
    names = z.namelist()
    check(all(not n.startswith("/") and ".." not in n.split("/") for n in names), f"{label}: entry names are relative")
    total = sum(i.file_size for i in z.infolist())
    check(total == exact_bytes(attachment) == int(attachment["size"]["amount"]),
          f"{label}: extracted size equals the attachment size ({total} bytes)",
          f"zip {total}, description {exact_bytes(attachment)}, amount {attachment['size']['amount']}")
    z.extractall(target)
    return z


def check_roleless_user(spec_id: str, stamp: str) -> None:
    """A valid token without ROLE_USER must get 403. Creates a throwaway Keycloak user."""
    r = requests.post(f"{KC}/realms/master/protocol/openid-connect/token", timeout=30, data={
        "grant_type": "password", "client_id": "admin-cli", "username": KC_ADMIN, "password": KC_ADMIN_PASSWORD})
    if r.status_code != 200:
        check(False, "403 without ROLE_USER (Keycloak admin login)", f"{r.status_code} {r.text[:200]}")
        return
    admin = {"Authorization": f"Bearer {r.json()['access_token']}"}
    name, password = f"artifact-verify-{stamp}", f"pw-{stamp}"
    r = requests.post(f"{KC}/admin/realms/{REALM}/users", headers=admin, timeout=30, json={
        "username": name, "enabled": True, "email": f"{name}@example.invalid", "emailVerified": True,
        "firstName": "Artifact", "lastName": "Verify",
        "credentials": [{"type": "password", "value": password, "temporary": False}]})
    if not check(r.status_code == 201, "403 without ROLE_USER (user created)", f"{r.status_code} {r.text[:200]}"):
        return
    roleless = Token(name, password)
    r = download(roleless, spec_id)
    check(r.status_code == 403, "token without ROLE_USER gets 403", f"{r.status_code} {r.text[:200]}")


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--clean-python", help="python of an empty virtual environment, for the requirements.txt check")
    parser.add_argument("--wait", type=int, default=240, help="seconds to wait for the catalog sync (default 240)")
    parser.add_argument("--keep", action="store_true",
                        help="keep the POSTed out-of-storage specification and the local work directory")
    args = parser.parse_args()

    import numpy as np
    stamp = time.strftime("%Y%m%d%H%M%S")
    experiment = f"artifact-verify-{stamp}"
    workdir = tempfile.mkdtemp(prefix="artifact-verify-")
    if not args.keep:
        atexit.register(shutil.rmtree, workdir, True)
    token = Token(USERNAME, PASSWORD)
    print(f"API {API}, MLflow {os.environ['MLFLOW_TRACKING_URI']}, work directory {workdir}")

    print("1. Train, convert to ONNX, log to MLflow")
    pipeline, X_test, predictions, ids = train_and_log(experiment, workdir)

    print(f"2. Wait for the catalog (up to {args.wait} s)")
    started = time.time()
    specs = find_specs(token, experiment, set(ids.values()), args.wait)
    if not check(len(specs) == 2, f"both models catalogued (after {time.time() - started:.0f} s)",
                 f"found {sorted(specs)}"):
        return report()
    spec, optout = specs[ids["onnx"]], specs[ids["optout"]]
    print(f"  specification {spec['id']} (onnx), {optout['id']} (downloadable=false)")

    print("3. Attachments")
    attachments = {a.get("attachmentType"): a for a in spec.get("attachment") or []}
    check(set(attachments) == {"mlflow-model", "onnx"}, "two attachments: mlflow-model and onnx", str(sorted(attachments)))
    for fmt, a in attachments.items():
        check(a.get("@type") == "ModelArtifactAttachment" and a.get("mimeType") == "application/zip",
              f"{fmt}: @type and mimeType", json.dumps(a))
        check(a.get("href", "").endswith(f"/aiModelSpecification/{spec['id']}/artifacts?format={fmt}"),
              f"{fmt}: href names the format", a.get("href", ""))
        check(a.get("size", {}).get("units") == "bytes" and a["size"].get("amount", 0) > 0, f"{fmt}: size in bytes",
              json.dumps(a.get("size")))
    if "onnx" in attachments:
        onnx_size = os.path.getsize(os.path.join(workdir, "model.onnx"))
        check(exact_bytes(attachments["onnx"]) == onnx_size, f"onnx: size is that of model.onnx ({onnx_size} bytes)",
              attachments["onnx"].get("description", ""))
    if "mlflow-model" in attachments and "onnx" in attachments:
        check(exact_bytes(attachments["mlflow-model"]) > exact_bytes(attachments["onnx"]),
              "mlflow-model is larger than onnx (it includes the ONNX copy)")
    characteristics = {c["name"]: c["characteristicValueSpecification"][0]["value"]
                       for c in spec.get("specCharacteristic") or [] if c.get("characteristicValueSpecification")}
    check(characteristics.get("platform") == "mlflow" and characteristics.get("artifactFormat") == "mlflow-model",
          "characteristics platform=mlflow, artifactFormat=mlflow-model",
          f"{characteristics.get('platform')}, {characteristics.get('artifactFormat')}")
    check(not optout.get("attachment"), "downloadable=false: no attachment", json.dumps(optout.get("attachment")))
    if set(attachments) != {"mlflow-model", "onnx"}:
        return report()

    print("4. Download mlflow-model and load it")
    import mlflow
    mlflow_dir = os.path.join(workdir, "download-mlflow")
    z = check_zip(download(token, spec["id"], "mlflow-model"), attachments["mlflow-model"], "mlflow-model", mlflow_dir)
    if z:
        names = set(z.namelist())
        check({"MLmodel", "requirements.txt", "extra_files/model.onnx"} <= names,
              "mlflow-model: holds MLmodel, requirements.txt, extra_files/model.onnx", str(sorted(names)))
        loaded = mlflow.pyfunc.load_model(mlflow_dir)
        check((np.asarray(loaded.predict(X_test)) == predictions).all(), "mlflow-model: pyfunc predicts like the pipeline")
        if args.clean_python:
            clean_load(args.clean_python, mlflow_dir, X_test, predictions, workdir)
        default = download(token, spec["id"])
        if check(default.status_code == 200, "no format: download is 200", str(default.status_code)):
            d = zipfile.ZipFile(io.BytesIO(default.content))
            check(sorted((i.filename, i.CRC) for i in d.infolist()) == sorted((i.filename, i.CRC) for i in z.infolist()),
                  "no format: same files as mlflow-model")
            check("-mlflow-model.zip" not in default.headers.get("Content-Disposition", "-mlflow-model.zip"),
                  "no format: file name without the format", default.headers.get("Content-Disposition", ""))

    print("5. Download onnx and run it")
    onnx_dir = os.path.join(workdir, "download-onnx")
    z = check_zip(download(token, spec["id"], "onnx"), attachments["onnx"], "onnx", onnx_dir)
    if z:
        check(z.namelist() == ["model.onnx"], "onnx: the zip holds only model.onnx", str(z.namelist()))
        import onnxruntime as ort
        session = ort.InferenceSession(os.path.join(onnx_dir, "model.onnx"))
        labels = session.run(None, {session.get_inputs()[0].name: X_test.to_numpy(dtype=np.float32)})[0]
        check((labels == predictions).all(), "onnx: ONNX Runtime predicts like the pipeline")

    print("6. Refusals")
    r = download(None, spec["id"])
    check(r.status_code == 401, "no token gets 401", str(r.status_code))
    if KC_ADMIN:
        check_roleless_user(spec["id"], stamp)
    else:
        print("  [SKIP] 403 without ROLE_USER (KEYCLOAK_ADMIN_USER is empty)")
    r = download(token, optout["id"])
    check(r.status_code == 404 and "downloadable=false" in r.text, "downloadable=false gets 404 naming it",
          f"{r.status_code} {r.text[:200]}")
    r = download(token, spec["id"], "pytorch")
    check(r.status_code == 404, "format=pytorch gets 404", f"{r.status_code} {r.text[:200]}")
    r = download(token, spec["id"], raw_query="?format=a%2Fb")
    check(r.status_code == 400, "format=a%2Fb gets 400", f"{r.status_code} {r.text[:200]}")
    outside_storage(token, args.keep)

    return report()


def clean_load(python: str, model_dir: str, X_test, predictions, workdir: str) -> None:
    """Step 3 of the plan: a fresh environment, pip install -r requirements.txt, load and predict."""
    print(f"  installing requirements.txt into {python} (may take minutes)")
    pip = subprocess.run([python, "-m", "pip", "install", "-q", "-r", os.path.join(model_dir, "requirements.txt"),
                          "pandas"], capture_output=True, text=True)
    if not check(pip.returncode == 0, "clean environment: pip install -r requirements.txt", pip.stderr[-500:]):
        return
    scripts = tempfile.mkdtemp(prefix="artifact-verify-loader-")
    atexit.register(shutil.rmtree, scripts, True)
    loader, frame = os.path.join(scripts, "load.py"), os.path.join(scripts, "input.json")
    with open(loader, "w") as f:
        f.write(CLEAN_LOADER)
    X_test.to_json(frame, orient="split")
    run = subprocess.run([python, "-I", loader, model_dir, frame], capture_output=True, text=True, cwd=workdir)
    ok = run.returncode == 0 and json.loads(run.stdout.strip().splitlines()[-1]) == [int(v) for v in predictions]
    check(ok, "clean environment: pyfunc predicts like the pipeline", (run.stderr or run.stdout)[-500:])


def outside_storage(token: Token, keep: bool) -> None:
    """A POSTed specification whose artifactUri is outside the allow-list must not be downloadable."""
    body = {"name": "artifact-verify-outside-storage", "version": "1",
            "specCharacteristic": [
                {"name": n, "valueType": "string", "characteristicValueSpecification": [{"value": v, "isDefault": True}]}
                for n, v in (("artifactUri", "s3://other/x"), ("platform", "mlflow"), ("artifactFormat", "mlflow-model"))]}
    r = requests.post(f"{API}/aiModelSpecification", headers=token.headers(), json=body, timeout=30)
    if not check(r.status_code == 201, "outside storage: POST is 201", f"{r.status_code} {r.text[:300]}"):
        return
    created = r.json()
    check(not created.get("attachment"), "outside storage: no attachment", json.dumps(created.get("attachment")))
    r = download(token, created["id"])
    check(r.status_code == 404, "outside storage: download gets 404", f"{r.status_code} {r.text[:200]}")
    check("other" not in r.text, "outside storage: 404 does not reveal the location", r.text[:200])
    if not keep:
        requests.delete(f"{API}/aiModelSpecification/{created['id']}", headers=token.headers(), timeout=30)


def report() -> int:
    failed = [w for ok, w in results if not ok]
    print(f"\n{len(results) - len(failed)}/{len(results)} checks passed")
    for w in failed:
        print(f"  FAILED: {w}")
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main())
