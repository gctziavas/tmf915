# MLflow Developer Guide — Training & Deploying via TMF 915

This guide explains what MLflow metadata you must record during training, how TMF 915 syncs that metadata into an `AiModelSpecification`, and how to instantiate and deploy a model via the TMF 915 API.

---

## 1. Workflow Overview

```
Training Notebook / Script
        │
        │  mlflow.log_model(...)
        ▼
MLflow Tracking Server  ──  MinIO (artifact store)
        │
        │  (periodic sync, every 60 s)
        ▼
TMF 915  →  AiModelSpecification  (blueprint/template)
        │
        │  POST /aiModel  {state: "reserved"}
        ▼
TMF 915  →  AiModel  (instance)
        │
        │  build Docker image  +  run container
        ▼
Serving endpoint  http://<host>:<port>/invocations
```

---

## 2. Required MLflow Metadata

The sync service reads the following from the **Logged Model** object. Log them explicitly during training so the resulting `AiModelSpecification` is complete.

### 2.1 Always required

| What to log | MLflow call | Stored as `specCharacteristic` |
|---|---|---|
| Experiment **name** | `mlflow.set_experiment("my-experiment")` | `name` field of the spec |
| Model **artifact** | `mlflow.sklearn.log_model(model, "model")` or equivalent | `loggedModelId`, `artifactUri` |
| Run **source** | automatic when using a `.ipynb` or `.py` file | `tags` (mlflow.source.name) |

### 2.2 Strongly recommended

| What to log | MLflow call | Stored as `specCharacteristic` |
|---|---|---|
| **Hyperparameters** | `mlflow.log_param("max_depth", 8)` | `param_<key>` |
| **Evaluation metrics** | `mlflow.log_metric("accuracy", 0.97)` | `metric_<key>` |
| **Model description** | `mlflow.set_tag("config.version", "v9_RF_MinMax")` | `description` of the spec |
| **Model type / flavour** | automatic from `log_model` call | `modelType` |
| **Author** | `mlflow.set_tag("mlflow.user", "alice")` | inside `tags` |

### 2.3 Special tags read by TMF 915

| Tag key | Purpose |
|---|---|
| `config.version` | Used as the spec **description** (first choice) |
| `config.model` | Fallback description if `config.version` is absent |
| `mlflow.source.name` | Notebook / script name, stored in `tags` characteristic |
| `mlflow.user` | Author, stored in `tags` characteristic |

> **Important:** Tags whose keys start with `mlflow.` are set automatically by the MLflow client library. You do not need to set them manually.

---

## 3. Full Training Example

Save this as `train_iris_classifier.py` (or use it as a notebook cell).

```python
import mlflow
import mlflow.sklearn
import numpy as np
from sklearn.datasets import load_iris
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler
from sklearn.pipeline import Pipeline
from sklearn.metrics import accuracy_score, f1_score

# ── 1. Connect to the tracking server ──────────────────────────────────────────
# These match the values in docker-compose / application.yml.
MLFLOW_TRACKING_URI = "http://172.16.100.94:5000"
MLFLOW_S3_ENDPOINT  = "http://172.16.100.94:9010"   # MinIO API port
EXPERIMENT_NAME     = "champion-pipeline-iris"       # becomes the spec name in TMF 915

import os
os.environ["MLFLOW_S3_ENDPOINT_URL"]  = MLFLOW_S3_ENDPOINT
os.environ["AWS_ACCESS_KEY_ID"]       = "minioadmin"   # adjust to your credentials
os.environ["AWS_SECRET_ACCESS_KEY"]   = "minioadmin"

mlflow.set_tracking_uri(MLFLOW_TRACKING_URI)
mlflow.set_experiment(EXPERIMENT_NAME)

# ── 2. Hyperparameters ─────────────────────────────────────────────────────────
HP = {
    "n_estimators": 100,
    "max_depth": 8,
    "random_state": 42,
}
DATA = {
    "test_size": 0.15,
    "val_size": 0.15,
}

# ── 3. Prepare data ─────────────────────────────────────────────────────────────
iris = load_iris()
X, y = iris.data, iris.target

X_train, X_test, y_train, y_test = train_test_split(
    X, y,
    test_size=DATA["test_size"],
    random_state=HP["random_state"],
)
X_train, X_val, y_train, y_val = train_test_split(
    X_train, y_train,
    test_size=DATA["val_size"] / (1 - DATA["test_size"]),
    random_state=HP["random_state"],
)

# ── 4. Train ────────────────────────────────────────────────────────────────────
with mlflow.start_run() as run:

    # — Tags read by TMF 915 sync ————————————————————————————————————————————
    version_label = f"v9_RandomForest_MinMaxScaler_{int((1-DATA['test_size']-DATA['val_size'])*100)}/{int(DATA['val_size']*100)}/{int(DATA['test_size']*100)}"
    mlflow.set_tag("config.version", version_label)          # → spec description
    mlflow.set_tag("config.model",   "RandomForest+MinMaxScaler")

    # — Hyperparameters ——————————————————————————————————————————————————————
    for k, v in HP.items():
        mlflow.log_param(f"hp.{k}", v)
    mlflow.log_param("data.n_train", len(X_train))
    mlflow.log_param("data.n_val",   len(X_val))
    mlflow.log_param("data.n_test",  len(X_test))

    # — Build and fit the pipeline ——————————————————————————————————————————
    pipeline = Pipeline([
        ("scaler", MinMaxScaler()),
        ("clf",    RandomForestClassifier(**HP)),
    ])
    pipeline.fit(X_train, y_train)

    # — Metrics ——————————————————————————————————————————————————————————————
    for split_name, X_s, y_s in [("val", X_val, y_val), ("test", X_test, y_test)]:
        preds = pipeline.predict(X_s)
        mlflow.log_metric(f"{split_name}_accuracy", accuracy_score(y_s, preds))
        mlflow.log_metric(f"{split_name}_f1",       f1_score(y_s, preds, average="macro"))

    # — Log the model ————————————————————————————————————————————————————————
    # The artifact is stored in MinIO under mlflow/<experiment_id>/models/<model_id>/artifacts
    # TMF 915 picks up this URI automatically via the sync service.
    mlflow.sklearn.log_model(
        pipeline,
        artifact_path="model",      # must be "model" for mlflow models build-docker
        registered_model_name=None, # use Logged Models (new MLflow 2.x style), not Model Registry
    )

    print(f"Run ID      : {run.info.run_id}")
    print(f"Logged model: check MLflow UI → Experiments → {EXPERIMENT_NAME}")
```

> **`artifact_path` must be `"model"`** — the deployment pipeline calls  
> `mlflow models build-docker --model-uri s3://.../artifacts` and expects the  
> `MLmodel` file to be at the root of that prefix.

---

## 4. Verify the Sync

After training, the TMF 915 sync service runs every 60 seconds. Check that your spec appeared:

```bash
# Get a token
TOKEN=$(curl -s -X POST http://localhost:8080/auth/realms/openslice/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=osapiWebClientId&username=admin&password=openslice" \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['access_token'])")

# List specs — look for your experiment name
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:13082/tmf-api/tmf-api/AiM/v4/aiModelSpecification" \
  | python3 -m json.tool | grep -E '"name"|"version"|"id"'
```

Note the `id` of your spec — you need it for the next step.

---

## 5. Deploy the Model

Once your spec is synced, create an `AiModel` instance in `reserved` state. The server will build a Docker image and start a container automatically.

```bash
SPEC_ID="<paste-your-spec-id-here>"

curl -s -X POST http://localhost:13082/tmf-api/tmf-api/AiM/v4/aiModel \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "aiModelSpecification": { "id": "'"$SPEC_ID"'" },
    "state": "reserved",
    "serviceCharacteristic": [
      { "name": "platform",    "valueType": "string", "value": "mlflow" },
      { "name": "dockerHost",  "valueType": "string", "value": "tcp://172.16.100.94:2375" }
    ]
  }' | python3 -m json.tool
```

> **Notes**
> - Omitting `name` is fine — the server derives it from the spec as `{specName}-v{specVersion}-{n}`.
> - Omitting `mlflowModelId` is fine — the server reads it from the spec's `loggedModelId` characteristic.
> - `dockerHost` must point to a Docker daemon that can pull from your MinIO/MLflow environment.

### Poll for completion

```bash
MODEL_ID="<id-from-POST-response>"

curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:13082/tmf-api/tmf-api/AiM/v4/aiModel/$MODEL_ID" \
  | python3 -c "
import sys, json
obj = json.load(sys.stdin)
sc = {c['name']: c.get('value') for c in (obj.get('serviceCharacteristic') or [])}
print('state   :', obj.get('state'))
print('endpoint:', sc.get('endpoint'))
print('notes   :', [n.get('text') for n in (obj.get('note') or [])])
"
```

The model is ready when `state` is `active` and `endpoint` is non-null.

---

## 6. Send an Inference Request

```bash
ENDPOINT="http://172.16.100.94:3001/invocations"   # from the endpoint characteristic

curl -s -X POST "$ENDPOINT" \
  -H "Content-Type: application/json" \
  -d '{
    "dataframe_split": {
      "columns": ["sepal length (cm)", "sepal width (cm)", "petal length (cm)", "petal width (cm)"],
      "data": [
        [5.1, 3.5, 1.4, 0.2],
        [6.7, 3.1, 4.7, 1.5]
      ]
    }
  }'
```

Expected response:

```json
{"predictions": [0, 1]}
```

---

## 7. Undeploy

PATCH the model to `inactive`. The server will stop and remove the container and image.

```bash
curl -s -X PATCH \
  "http://localhost:13082/tmf-api/tmf-api/AiM/v4/aiModel/$MODEL_ID" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"state": "inactive"}'
```

---

## 8. Troubleshooting

| Symptom | Likely cause | Fix |
|---|---|---|
| Model stays `reserved`, note says "Image build failed, model files not found" | Artifact URI in MinIO is wrong or the `model` sub-directory is missing | Check that `artifact_path="model"` was used; verify in MinIO console (`http://<host>:9011`) |
| Model stays `reserved` with no note | Docker image build is still running (can take several minutes) | Wait and poll again |
| `state` → `designed`, note says "Build failed (exit 1)" | MLflow dependency conflict during `build-docker` | Check container logs: `docker logs tmf915-tmf915-1 2>&1 \| tail -200` |
| 400 on POST /aiModel | Missing required field — often `software` | Not needed if the server auto-injects the `mlflow` software ref (it does when `platform=mlflow`) |
| Spec not appearing after training | Sync not yet run, or experiment ID not in the watched list | Wait up to 60 s; check `mlflow.sync.experiment-ids` in `application.yml` |
