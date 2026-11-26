# Model publishing guide

How to get your trained model into the TMF 915 catalog and served through the API. You log the model to MLflow as usual; the API picks it up, publishes it as an `aiModelSpecification`, and deploys it as an HTTP endpoint when someone asks for it.

```
your training code ──log_model──▶ MLflow + MinIO ──sync, ~1 min──▶ aiModelSpecification ──deploy──▶ /invocations
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

| | Requirement | Why |
|---|---|---|
| 1 | Logged with an MLflow 3 flavor's `log_model` (`mlflow.sklearn.log_model`, `mlflow.pyfunc.log_model`, …) to the **tracking server the API uses** | The API discovers MLflow 3 *logged models*. Models in the classic Model Registry are picked up only when the server has no logged models at all. |
| 2 | Artifacts uploaded to the **MinIO/S3** store the API is configured with | Models without artifacts are skipped (and retried every 10 minutes). |
| 3 | Experiment named meaningfully with `mlflow.set_experiment(...)` | The experiment name becomes the specification's **name** in the catalog. |
| 4 | A **signature** or **input example** logged with the model | The API generates the example request body (`inferencePayloadExample`) from it. Without one, users get a placeholder they cannot use. |
| 5 | The model can be served by `mlflow models build-docker` | This is how the API builds the serving image. Anything `mlflow models serve` can serve works. |

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
    )

    # Metrics linked to the logged model appear in the catalog
    mlflow.log_metric("test_accuracy", accuracy_score(y_test, predictions), model_id=model_info.model_id)
    mlflow.log_metric("test_f1", f1_score(y_test, predictions, average="macro"), model_id=model_info.model_id)

print("Logged model:", model_info.model_id)   # m-…, becomes the catalog version
```

Notes on the example:

- **`params`:** the values passed to `log_model(params=...)` are stored on the logged model, which is where the API reads them. Parameters logged only to the run with `mlflow.log_param` may not reach the catalog.
- **Pandas input:** training on a DataFrame gives the signature real column names, so the example request body uses them.

---

## 3. What ends up in the catalog

Each logged model becomes one `aiModelSpecification`:

| Catalog field | Comes from |
|---|---|
| `name` | Experiment name |
| `version` | Logged model id (`m-…`) |
| `description` | Param `config.version`; else `"MLflow logged model: <config.model> (<id>)"`; else `"MLflow logged model: <id>"` |
| `modelDataSheet` | Artifact URI |

And these `specCharacteristic` entries:

| Characteristic | Content |
|---|---|
| `loggedModelId`, `experimentId`, `runId` | MLflow identifiers (`loggedModelId` is what deployment uses) |
| `modelName` | The `name` given to `log_model` |
| `modelType` | Model type, if set |
| `artifactUri` | Where the artifacts are stored |
| `mlflowUrl` | Link to the model in the MLflow UI |
| `creationTimestamp`, `lastUpdatedTimestamp` | Epoch milliseconds |
| `param_<key>` | Every param on the logged model |
| `metric_<key>` | Metrics whose name contains `accuracy`, `precision`, `recall`, `f1`, `auc`, `loss`, `mse`, `rmse`, `mae`, `r2` or `score`; others are not shown |
| `tags` | Logged-model tags as `key=value, …` |
| `model_file_exists` | Set to `true`/`false` after a deployment attempt |

The catalog entry is created once and not updated afterwards. Changing params or tags on an existing logged model in MLflow does not change the catalog; log a new model instead.

---

## 4. Check that your model was published

Within about a minute (the sync runs every minute):

```bash
# See the user guide, section 2, for getting $TOKEN
curl -s -H "Authorization: Bearer $TOKEN" \
  "http://localhost:13082/tmf-api/AiM/v4/aiModelSpecification?fields=name,version,description&limit=200" \
  | python3 -m json.tool | grep -B2 -A2 '"iris-classifier"'
```

Note the `id`; you need it to deploy. If the model doesn't appear, see [troubleshooting](#7-troubleshooting).

---

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

---

## 6. Publishing a new version

Log the model again, in the same experiment. Each `log_model` call creates a new logged model with a new id, so it appears as a **new specification** with the same `name` and a new `version`. Existing specifications and running deployments are not affected.

To remove a version from the catalog: `DELETE /aiModelSpecification/<SPEC_ID>`. Also delete the logged model in MLflow: otherwise it is published again the next time the API restarts.

---

## 7. Troubleshooting

| Symptom | Cause and fix |
|---|---|
| Model never appears in the catalog | It was logged to a different MLflow server than the API's `mlflow.tracking-uri`; or its artifacts are not in MinIO (log: `no artifacts were found … Backing off for 10 minutes`); or it is only in the classic Model Registry while the server also has logged models. |
| Catalog `description` is generic | `config.version` was not passed in `log_model(params=...)`. |
| `inferencePayloadExample` contains `WARNING: Could not extract signature` | No signature was logged. Log the model again with `signature=` or `input_example=`. |
| Deployment goes back to `designed`, note "model files not found" | Artifacts missing or unreadable in MinIO. Check the bucket in the MinIO console (http://localhost:9011). |
| Deployment goes back to `designed`, note "Build failed" | `mlflow models build-docker` could not build your environment, usually an unpinned or unavailable dependency. Run `mlflow models build-docker -m <artifactUri> -n test` yourself to see the error, and pin versions with `pip_requirements=`. |
| Endpoint returns `400` | The request body doesn't match the signature. Start from `inferencePayloadExample` and replace the placeholder values. |
