# Model publishing guide

How to get your trained model into the TMF 915 catalog and served through the API. You log the model to MLflow as usual; the API picks it up, publishes it as an `aiModelSpecification`, and deploys it as an HTTP endpoint when someone asks for it.

```
your training code ──log_model──▶ MLflow + MinIO ──sync, 1–2 min──▶ aiModelSpecification ──deploy──▶ /invocations
```

No change to the API is needed per model.

## Contents

1. [Checklist](#1-checklist)
2. [Example](#2-example)
3. [What ends up in the catalog](#3-what-ends-up-in-the-catalog)
4. [Check that your model was published](#4-check-that-your-model-was-published)
5. [Deploy and call it](#5-deploy-and-call-it)
6. [Publishing a new version](#6-publishing-a-new-version)
7. [Troubleshooting](#7-troubleshooting)

---

## 1. Checklist

Your model is published, and deploys correctly, if all of these hold:


|   | Requirement                                                                                                                                     | Why                                                                                                                                             |
| - | ----------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------- |
| 1 | Logged with an MLflow 3 flavor's`log_model` (`mlflow.sklearn.log_model`, `mlflow.pyfunc.log_model`, …) to the **tracking server the API uses** | The API discovers MLflow 3*logged models*. Models in the classic Model Registry are picked up only when the server has no logged models at all. |
| 2 | Artifacts uploaded to the**MinIO/S3** store the API is configured with                                                                          | Models without artifacts are skipped (and retried every 10 minutes).                                                                            |
| 3 | Experiment named meaningfully with`mlflow.set_experiment(...)`                                                                                  | The experiment name becomes the specification's**name** in the catalog.                                                                         |
| 4 | A **signature** or **input example** logged with the model                                                                                     | The API generates the example request body (`inferencePayloadExample`) from it. Without one, users get a placeholder they cannot use.           |
| 5 | The model can be served by`mlflow models build-docker`                                                                                          | This is how the API builds the serving image. Anything`mlflow models serve` can serve works.                                                    |

Recommended: pass a `config.version` (or `config.model`) entry in `params` to become the catalog **description**, log the evaluation metrics you want consumers to see, and pin your dependencies (`pip_requirements`) so the image builds reproducibly.

---

## 2. Example

```python
import os
import mlflow
from mlflow.models import infer_signature
from sklearn.datasets import load_iris
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, f1_score
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import MinMaxScaler

# The same MLflow and MinIO the API uses (see application.yml / docker-compose.yml)
os.environ["MLFLOW_S3_ENDPOINT_URL"] = "http://localhost:9010"
os.environ["AWS_ACCESS_KEY_ID"] = "minioadmin"
os.environ["AWS_SECRET_ACCESS_KEY"] = "minioadmin"
mlflow.set_tracking_uri("http://localhost:5000")
mlflow.set_experiment("iris-classifier")                 # catalog name

iris = load_iris(as_frame=True)
X_train, X_test, y_train, y_test = train_test_split(iris.data, iris.target, test_size=0.2, random_state=42)

hp = {"n_estimators": 100, "max_depth": 8, "random_state": 42}
pipeline = Pipeline([("scaler", MinMaxScaler()), ("clf", RandomForestClassifier(**hp))])

with mlflow.start_run():
    pipeline.fit(X_train, y_train)
    predictions = pipeline.predict(X_test)

    model_info = mlflow.sklearn.log_model(
        pipeline,
        name="model",
        params={**hp, "config.version": "RandomForest + MinMaxScaler, 80/20 split"},  # description
        signature=infer_signature(X_test, predictions),   # → inferencePayloadExample
        input_example=X_test.head(2),
        skops_trusted_types=["sklearn.tree._tree.Tree"],   # see the note below
    )

    # Metrics linked to the logged model appear in the catalog
    mlflow.log_metric("test_accuracy", accuracy_score(y_test, predictions), model_id=model_info.model_id)
    mlflow.log_metric("test_f1", f1_score(y_test, predictions, average="macro"), model_id=model_info.model_id)

print("Logged model:", model_info.model_id)   # m-…, becomes the catalog version
```

Notes on the example:

- **`params`:** the values passed to `log_model(params=...)` are stored on the logged model, which is where the API reads them. Parameters logged only to the run with `mlflow.log_param` may not reach the catalog.
- **Metrics:** log them right after `log_model`, as above. The catalog entry is created once, one minute after `log_model` finished (`aimodel.catalog.sync.settle-ms`), and never updated: metrics linked to the model later do not reach the catalog. Compute everything slow before `log_model`.
- **Pandas input:** training on a DataFrame gives the signature real column names, so the example request body uses them.
- **`skops_trusted_types`:** recent MLflow 3 releases (3.18 when this was checked) save scikit-learn models in the `skops` format and refuse to log a model containing types not explicitly trusted. Tree-based models (decision trees, random forests, gradient boosting) need `sklearn.tree._tree.Tree`. If `log_model` fails with "references untrusted types", trust exactly the types the message lists. If your MLflow version does not know this argument, remove it.

## 3. What ends up in the catalog

Each logged model becomes one `aiModelSpecification`:


| Catalog field    | Comes from                                                                                                     |
| ---------------- | -------------------------------------------------------------------------------------------------------------- |
| `name`           | Experiment name                                                                                                |
| `version`        | Logged model id (`m-…`)                                                                                       |
| `description`    | Param`config.version`; else `"MLflow logged model: <config.model> (<id>)"`; else `"MLflow logged model: <id>"` |
| `modelDataSheet` | Artifact URI                                                                                                   |
| `lifecycleStatus` | `Active` (the server setting `aimodel.specification.default-lifecycle-status`)                               |

And these `specCharacteristic` entries:


| Characteristic                              | Content                                                                                                                                        |
| ------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| `loggedModelId`, `experimentId`, `runId`    | MLflow identifiers (`loggedModelId` is what deployment uses)                                                                                   |
| `modelName`                                 | The`name` given to `log_model`                                                                                                                 |
| `modelType`                                 | Model type, if set                                                                                                                             |
| `artifactUri`                               | Where the artifacts are stored                                                                                                                 |
| `mlflowUrl`                                 | Link to the model in the MLflow UI                                                                                                             |
| `creationTimestamp`, `lastUpdatedTimestamp` | Epoch milliseconds                                                                                                                             |
| `param_<key>`                               | Every param on the logged model                                                                                                                |
| `metric_<key>`                              | Metrics whose name contains`accuracy`, `precision`, `recall`, `f1`, `auc`, `loss`, `mse`, `rmse`, `mae`, `r2` or `score`; others are not shown |
| `tags`                                      | Logged-model tags as`key=value, …`                                                                                                            |
| `model_file_exists`                         | Set to`true`/`false` after a deployment attempt                                                                                                |
| `platform`, `artifactFormat`                | `mlflow` and `mlflow-model`: which platform the model comes from, and the format clients download it in                                         |
| `downloadable`                              | Only if you set the tag `downloadable` (see below)                                                                                              |

The catalog entry is created once and not updated afterwards. Changing params or tags on an existing logged model in MLflow does not change the catalog; log a new model instead.

### Downloads, and how to opt out

By default, any API user with `ROLE_USER` can download your model's files and load them in their own code (see the [integration guide](INTEGRATION_GUIDE.md#5-using-a-model-as-a-library)). The specification then has an `attachment` of `@type` `ModelArtifactAttachment` with the download link, the format and the size.

**What they receive:** a zip of everything under the logged model's artifact URI: `MLmodel`, the model files, `requirements.txt`, `conda.yaml`, `python_env.yaml` and any input example. Anything else you save into the model's directory is included too, so keep credentials and private data out of it.

**To keep a model from being downloaded** (licence, confidential weights), give it the tag `downloadable` = `false` **when you log it**:

```python
model_info = mlflow.sklearn.log_model(..., tags={"downloadable": "false"})
```

In the classic Model Registry, tag the version or the registered model (a version's tag wins):

```python
from mlflow import MlflowClient
MlflowClient().set_registered_model_tag("churn", "downloadable", "false")
```

The tag is read when the catalog entry is created, one to two minutes after logging. For a model that is already in the catalog, set the characteristic on the specification instead:

```bash
curl -s -X PATCH -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"specCharacteristic":[{"name":"downloadable","characteristicValueSpecification":[{"value":"false","isDefault":true}]}]}' \
  "http://localhost:13082/tmf-api/AiM/v4/aiModelSpecification/<SPEC_ID>"
```

The download then answers `404` with the reason, and the attachment disappears. Setting it back to `true` (or removing it) makes the model downloadable again at once, but the attachment only reappears after the API's next restart. Opting out does not affect deployment: the model can still be deployed and called through its endpoint.

### Also offer an ONNX version

An MLflow model can only be loaded with Python and the libraries it was trained with (`requirements.txt`). An [ONNX](https://onnx.ai/) copy of the same model can be run with [ONNX Runtime](https://onnxruntime.ai/) from Python, Java, C#, C++ or JavaScript, without scikit-learn, PyTorch or MLflow, and is usually smaller and faster to load. If you add one, consumers can choose: the catalog entry then offers two downloads, `mlflow-model` and `onnx`.

The API does not convert models; you add the ONNX file when you log the model, with `extra_files`. It must be called **`model.onnx`**. For the example in section 2 (`pip install skl2onnx onnxruntime`):

```python
import numpy as np
from skl2onnx import to_onnx

# Convert the fitted pipeline; the sample fixes the input type and width (4 float features).
onx = to_onnx(pipeline, X_train.to_numpy(dtype=np.float32)[:1])
with open("model.onnx", "wb") as f:
    f.write(onx.SerializeToString())

# Check the copy predicts like the original before publishing it.
import onnxruntime as ort
session = ort.InferenceSession("model.onnx")
onnx_labels = session.run(None, {session.get_inputs()[0].name: X_test.to_numpy(dtype=np.float32)})[0]
assert (onnx_labels == pipeline.predict(X_test)).all()

model_info = mlflow.sklearn.log_model(
    pipeline,
    name="model",
    ...,                                   # as in section 2
    extra_files=["model.onnx"],            # → offered as the "onnx" format
)
```

MLflow stores the file as `extra_files/model.onnx` in the model directory, which is where the API looks. Consumers download it with `GET /aiModelSpecification/<SPEC_ID>/artifacts?format=onnx`: a zip holding just `model.onnx`.

- **Flavors:** `extra_files` exists for `sklearn`, `pytorch`, `xgboost`, `lightgbm`, `catboost`, `statsmodels` and `onnx` (checked with MLflow 3.10). `pyfunc` and `transformers` models cannot offer an ONNX copy this way.
- **Converters:** `skl2onnx` (scikit-learn), `torch.onnx.export` (PyTorch), `onnxmltools` (XGBoost, LightGBM, CatBoost). Not every model converts; check predictions as above.
- **One file:** the ONNX file must be self-contained. Models over 2 GB, which ONNX stores with separate weight files, are not supported.
- **Size limit:** each format counts separately against the server's limit (2 GB by default). The full MLflow model includes the ONNX copy, so it is the larger download.
- **When:** only at logging time. A model catalogued without the file does not get the ONNX download later; log a new version.

## 4. Check that your model was published

Within one to two minutes. The sync runs every minute, and publishes a model only once MLflow reports it ready and one minute has passed since, so that the metrics you log right after `log_model` are included:

```bash
# See the user guide, section 2, for getting $TOKEN
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:13082/tmf-api/AiM/v4/aiModelSpecification?fields=name,version,description&limit=200" \
  | python3 -m json.tool | grep -B2 -A2 '"iris-classifier"'
```

Note the `id`; you need it to deploy. If the model doesn't appear, see [troubleshooting](#7-troubleshooting).

## 5. Deploy and call it

```bash
API=http://localhost:13082/tmf-api/AiM/v4

curl -s -X POST "$API/aiModel" -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d '{
  "state": "reserved",
  "aiModelSpecification": { "id": "<SPEC_ID>" },
  "software": [ { "id": "mlflow" } ],
  "serviceCharacteristic": [ { "name": "platform", "valueType": "string", "value": "mlflow" } ]
}'
```

When the model's `state` is `active` (poll `GET $API/aiModel/<MODEL_ID>`), its characteristics include:

- **`endpoint`:** the URL to POST predictions to, for example `http://localhost:3000/invocations`.
- **`inferencePayloadExample`:** a request body built from your signature, for example:

```json
{ "dataframe_split": { "columns": ["sepal length (cm)", "sepal width (cm)", "petal length (cm)", "petal width (cm)"],
                       "data": [[0.0, 0.0, 0.0, 0.0]] } }
```

```bash
curl -s -X POST "<endpoint>" -H "Content-Type: application/json" \
  -d '{"dataframe_split":{"columns":["sepal length (cm)","sepal width (cm)","petal length (cm)","petal width (cm)"],"data":[[5.1,3.5,1.4,0.2]]}}'
# {"predictions": [0]}
```

The endpoint is the standard MLflow scoring server, so every input format it accepts works (`dataframe_split`, `dataframe_records`, `instances`, `inputs`). The [user guide](USER_GUIDE.md#4-deploy-a-model) covers deployment options, such as which Docker host to use and how long the model stays up.

## 6. Publishing a new version

Log the model again, in the same experiment. Each `log_model` call creates a new logged model with a new id, so it appears as a **new specification** with the same `name` and a new `version`. Existing specifications and running deployments are not affected.

To remove a version from the catalog: `DELETE /aiModelSpecification/<SPEC_ID>`. Also delete the logged model in MLflow: otherwise it is published again the next time the API restarts.

## 7. Troubleshooting


| Symptom                                                                   | Cause and fix                                                                                                                                                                                                                                                          |
| ------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Model never appears in the catalog                                        | It was logged to a different MLflow server than the API's`mlflow.tracking-uri`; or its artifacts are not in MinIO (log: `no artifacts were found … Backing off for 10 minutes`); or it is only in the classic Model Registry while the server also has logged models; or MLflow still reports it pending (log: `the platform is still writing it`), which stays so for good if the logging script stopped during `log_model`: log the model again; or its upload failed (log: `writing it failed`). |
| Catalog`description` is generic                                           | `config.version` was not passed in `log_model(params=...)`.                                                                                                                                                                                                            |
| A metric is in MLflow but not in the catalog | It was linked to the model more than `aimodel.catalog.sync.settle-ms` (one minute) after `log_model`, after the catalog entry was created; or its name contains none of the words listed in [section 3](#3-what-ends-up-in-the-catalog). Log it right after `log_model`, then log the model again to get a new catalog entry. |
| `inferencePayloadExample` contains `WARNING: Could not extract signature` | No signature was logged. Log the model again with`signature=` or `input_example=`.                                                                                                                                                                                     |
| Deployment goes back to `designed`, note "Image build failed: model files not found" | Artifacts missing or unreadable in MinIO. Check the bucket in the MinIO console (http://localhost:9011). |
| Deployment goes back to `designed`, note "Image build failed" | `mlflow models build-docker` could not build your environment, usually an unpinned or unavailable dependency. The note has a reference; the operator finds the build output in the server log with it. Or run `mlflow models build-docker -m <artifactUri> -n test` yourself, and pin versions with `pip_requirements=`. |
| No `onnx` attachment although you added an ONNX copy | The file is not named `model.onnx`, was not passed with `extra_files` (check that the MLflow UI shows `extra_files/model.onnx` under the model's artifacts), the model was logged with a flavor without `extra_files`, or it is above the size limit. |
| Specification has no `ModelArtifactAttachment`                            | The model opted out (`downloadable=false`); or its artifacts are outside the storage the API may read (`aimodel.artifacts.allowed-locations`, the `mlflow` bucket by default); or they are larger than `aimodel.artifacts.max-download-bytes`; or the API has no S3 settings. Models catalogued before downloads existed get the attachment after the API's next restart. |
| Endpoint returns`400`                                                     | The request body doesn't match the signature. Start from`inferencePayloadExample` and replace the placeholder values.                                                                                                                                                  |
