# User guide

How to run the TMF 915 AI Management API and use it: get a token, browse the model catalog, deploy a model, call it, and stop it. For publishing your own model, see the [model publishing guide](MODEL_PUBLISHING_GUIDE.md). For building software on top of the API, see the [integration guide](INTEGRATION_GUIDE.md).

## Contents

1. [Run the platform](#1-run-the-platform)
2. [Get an access token](#2-get-an-access-token)
3. [Browse the catalog](#3-browse-the-catalog)
4. [Deploy a model](#4-deploy-a-model)
5. [Call the model](#5-call-the-model)
6. [Stop or delete a model](#6-stop-or-delete-a-model)
7. [Lifecycle at a glance](#7-lifecycle-at-a-glance)
8. [Configuration](#8-configuration)
9. [Troubleshooting](#9-troubleshooting)

---

## 1. Run the platform

The stack runs with Docker Compose. The `tmf915` image builds the application from source, so you only need Docker.

| Service | Port | Purpose |
|---|---|---|
| `tmf915` | 13082 | The API |
| `mysql` | 3306 | Database for the API (`aim915db`) and Keycloak |
| `keycloak` | 8080 | Issues access tokens (realm `openslice`) |
| `mlflow` | 5000 | Model tracking server (`full` profile only) |
| `minio` | 9010 (API), 9011 (console) | Artifact storage for MLflow (`full` profile only) |
| `minio-init` | none | Creates the `mlflow` bucket, then exits (`full` profile only) |

### 1.1 Token issuer

The API only accepts tokens whose issuer is exactly the configured URL, and Keycloak writes into each token the address it was called on. The default, `http://localhost:8080/auth/realms/openslice`, is right when clients get their tokens on the same machine. If they use another address (another host, a DNS name), set it before starting:

```bash
export KEYCLOAK_ISSUER_URI=http://my-server.example.com:8080/auth/realms/openslice
```

If the two don't match, every API call returns `401`.

Swagger UI signs in through `localhost:8080` too. For Swagger from another machine, also set `SPRINGDOC_OAUTHFLOW_AUTHORIZATIONURL` and `SPRINGDOC_OAUTHFLOW_TOKENURL` on the `tmf915` service, for example `http://my-server.example.com:8080/auth/realms/openslice/protocol/openid-connect/auth` and `…/token`.

### 1.2 Start

**Everything local** (MySQL, Keycloak, MinIO, MLflow, API):

```bash
docker compose --profile full up -d --build
```

MinIO no longer publishes container images, so the `full` profile uses unmodified mirrors of the last official builds (`gtziavas/minio-mirror`, `gtziavas/mc-mirror`). They exist for linux/amd64 only; on ARM machines (Apple Silicon, ARM servers) Docker has to emulate them, which is slower, or point `MLFLOW_S3_ENDPOINT_URL` at an S3 store of your own.

The one-shot `minio-init` service creates the `mlflow` bucket that MLflow stores artifacts in (`s3://mlflow/`), then exits; `mlflow` starts after it. The MinIO console is at http://localhost:9011 (`minioadmin` / `minioadmin`).

**With an existing MLflow and MinIO:** start only MySQL, Keycloak and the API, and point the API at your servers:

```bash
export MLFLOW_TRACKING_URI=http://<mlflow-host>:5000
export MLFLOW_S3_ENDPOINT_URL=http://<minio-host>:9010
docker compose up -d --build
```

If you don't set them, the API looks for the `full` profile's services (`http://mlflow:5000`, `http://minio:9000`). To run without MLflow at all, set `MLFLOW_ENABLED=false` in `docker-compose.yml`: the catalog and models can then be managed, but nothing is synced or deployed.

The first start takes several minutes: MySQL initialises its data, and Keycloak creates its database schema before it answers on port 8080. The API waits for MySQL; requests that need a token fail until Keycloak is up. Later starts are quick.

### 1.3 Check it works

```bash
docker compose ps                                   # all services Up, tmf915 healthy
curl http://localhost:13082/tmf-api/health          # {"status":"UP"}
```

| What | Where |
|---|---|
| Swagger UI (try every call in the browser) | http://localhost:13082/tmf-api/swagger-ui.html |
| Keycloak admin console | http://localhost:8080/auth (`kcadmin` / `kcadmin`) |
| MLflow UI | http://localhost:5000 |

In Swagger UI, click **Authorize**, choose the `keycloak` scheme with client `osapiWebClientId`, and sign in as `admin` / `openslice`.

### 1.4 Stop

```bash
docker compose down          # stop, keep data
docker compose down -v       # stop and delete all data (MySQL, MinIO)
```

---

## 2. Get an access token

Every API call needs a bearer token from Keycloak. The demo realm has one user, `admin` / `openslice`, with the required role `ROLE_USER`.

```bash
TOKEN=$(curl -s -X POST "http://localhost:8080/auth/realms/openslice/protocol/openid-connect/token" \
  -d grant_type=password -d client_id=osapiWebClientId \
  -d username=admin -d password=openslice | python3 -c 'import sys,json; print(json.load(sys.stdin)["access_token"])')

API=http://localhost:13082/tmf-api/AiM/v4
```

Tokens expire after a few minutes. Run the command again when you get `401`.

All examples below use `$TOKEN` and `$API`. The same calls are in the Postman collection in [`postman/`](../postman/); import it, run request 1 to get a token, then follow the numbered requests.

---

## 3. Browse the catalog

Each **AI model specification** is a model that can be deployed. Specifications are created automatically from MLflow, about one minute after a model is logged (see the [model publishing guide](MODEL_PUBLISHING_GUIDE.md)).

```bash
# A compact list: only the fields you ask for (id, href and @type are always included)
curl -s -H "Authorization: Bearer $TOKEN" \
  "$API/aiModelSpecification?fields=name,version,description,lifecycleStatus"

# Everything about one specification
curl -s -H "Authorization: Bearer $TOKEN" "$API/aiModelSpecification/<SPEC_ID>"
```

- **Name and version:** a specification's `name` is the MLflow experiment name, and its `version` is the MLflow model id (`m-…`).
- **Paging:** lists return 50 items by default. Use `limit` and `offset` to page, for example `?offset=50&limit=50`.
- **`fields`:** without `fields`, list responses contain every attribute, which can be large.

---

## 4. Deploy a model

Create an **AI model** from a specification, in state `reserved`:

```bash
curl -s -X POST "$API/aiModel" \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{
    "state": "reserved",
    "aiModelSpecification": { "id": "<SPEC_ID>" },
    "software": [ { "id": "mlflow" } ],
    "serviceCharacteristic": [
      { "name": "platform", "valueType": "string", "value": "mlflow" }
    ]
  }'
```

| Field | Required | Notes |
|---|---|---|
| `state: "reserved"` | yes, to deploy | `designed` creates the record without deploying; deploy later with `PATCH {"state":"reserved"}`. You cannot create a model as `active`, `inactive` or `terminated`. |
| `aiModelSpecification.id` | yes | From the catalog. |
| `software` | yes | At least one entry; `[{"id": "mlflow"}]` is enough. Without it, the request fails with `400`. |
| `platform = mlflow` | yes | Selects the MLflow deployer. **Without it, the model stays `reserved` and is never deployed.** |
| `dockerHost` characteristic | no | Docker daemon to run the container on, e.g. `tcp://10.0.0.5:2375`. Default: `mlflow.docker.host` (the local Docker socket in Compose). Any other value must be listed in `mlflow.docker.allowed-hosts`, or the request fails with `400`. |
| `name` | no | Default: `<specName>-v-<specVersion>-<n>`. |
| `startDate`, `endDate` | no | Default: now, and **now + 1 day**. The model is undeployed automatically at `endDate`. Set it explicitly to keep a model longer; a future `startDate` schedules the deployment. |

Characteristics the server writes after a deployment (`containerId`, `imageName`, `hostPort`, `endpoint`, `deployedAt`, `inferencePayloadExample`) are ignored if you send them.

The response comes back at once with state `reserved`. Building the serving image can take several minutes. Check progress:

```bash
curl -s -H "Authorization: Bearer $TOKEN" "$API/aiModel/<MODEL_ID>?fields=state,serviceCharacteristic,note"
```

| State | Meaning |
|---|---|
| `reserved` | Waiting for `startDate`, or building and starting. |
| `active` | Running. The `endpoint` and `inferencePayloadExample` characteristics are set. |
| `designed` | Deployment failed. The reason is in `note` (most recent last). Fix it and `PATCH` back to `reserved`. |

To list all models: `GET $API/aiModel` (same `offset`, `limit` and `fields` options as the catalog).

---

## 5. Call the model

Read two characteristics from the active model:

- **`endpoint`:** the prediction URL, `http://<docker-host>:<port>/invocations`. With the local Docker socket the host is `localhost`, which only works on the machine running Docker, unless the operator sets `aimodel.deployment.serving-base-url` (see section 8).
- **`inferencePayloadExample`:** a request body built from the model's input signature, with placeholder values.

```bash
curl -s -X POST "http://localhost:3000/invocations" \
  -H "Content-Type: application/json" \
  -d '{ "dataframe_split": { "columns": ["sepal length (cm)", "sepal width (cm)", "petal length (cm)", "petal width (cm)"],
                             "data": [[5.1, 3.5, 1.4, 0.2]] } }'
# {"predictions": [0]}
```

The endpoint is the MLflow model server running in the container. It is **not** behind the API's authentication, so anyone who can reach that host and port can call it.

---

## 6. Stop or delete a model

```bash
# Stop: removes the container (and the image, unless another model uses it); state becomes terminated (record kept)
curl -s -X PATCH "$API/aiModel/<MODEL_ID>" -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" -d '{"state":"inactive"}'

# Delete: also removes the container if it is still running
curl -s -X DELETE "$API/aiModel/<MODEL_ID>" -H "Authorization: Bearer $TOKEN"
```

---

## 7. Lifecycle at a glance

```
POST state=designed ──▶ designed ──PATCH state=reserved──┐
POST state=reserved ─────────────────────────────────────┴─▶ reserved ──deployed──▶ active
                                                                 │                    │
                                                    deploy fails │                    │ PATCH state=inactive,
                                                                 ▼                    │ or endDate reached
                                                             designed                 ▼
                                                           (reason in note)       terminated
```

- **Restarts:** after a restart, the API re-schedules `reserved` models and the automatic teardown of `active` ones.
- **Allowed state changes:** with `PATCH` you can request only `reserved` (from `feasibilityChecked`, `designed` or `terminated`) and `inactive` (from `active` or `reserved`). Anything else returns `400`. To redeploy an active model, set it to `inactive`, then to `reserved`. Sending the model's current state is accepted and changes nothing.
- **Other changes:** other fields can be changed with `PATCH` at any time. `serviceCharacteristic` entries are merged by `name`.

---

## 8. Configuration

Settings live in `src/main/resources/application.yml`. In Docker Compose, override any of them with environment variables: upper case, with `.` and `-` replaced by `_` (for example `mlflow.sync.interval-ms` becomes `MLFLOW_SYNC_INTERVAL_MS`).

| Setting | Default | What it does |
|---|---|---|
| `spring.datasource.url`, `username`, `password` | local MySQL | Database. Tables are created automatically. |
| `keycloak.issuer-uri` | per profile | Token issuer the API accepts; must match the tokens exactly (see 1.1). |
| `spring.security.oauth2.resourceserver.jwt.jwk-set-uri` | Keycloak certs URL | Where the API fetches Keycloak's signing keys. |
| `mlflow.enabled` | `true` | MLflow integration. With `false`, the API runs without MLflow: specifications and models can be created and read, but nothing is synced from MLflow or deployed. |
| `mlflow.tracking-uri` | `http://127.0.0.1:5000` | MLflow server. |
| `mlflow.s3-endpoint-url`, `s3-access-key`, `s3-secret-key` | empty | MinIO/S3 holding the model artifacts. |
| `mlflow.sync.enabled` | `true` | Publish MLflow models to the catalog automatically. |
| `mlflow.sync.interval-ms` | `60000` | How often to look for new models. |
| `mlflow.sync.full-interval-ms` | `600000` | How often to re-read all models (in between, only new ones are fetched). |
| `mlflow.docker.host` | local socket | Default Docker daemon for deployments. |
| `mlflow.docker.allowed-hosts` | empty | Other Docker daemons clients may name in the `dockerHost` characteristic, comma-separated (for example `tcp://10.0.0.5:2375,tcp://10.0.0.6:2375`). `mlflow.docker.host` is always allowed. |
| `mlflow.docker.host-port-start`, `host-port-end` | `3000`–`7000` | Port range for model endpoints. |
| `mlflow.docker.env-manager` | `local` | Python environment used to build images (`local`, `virtualenv`, `uv`). |
| `mlflow.docker.command-timeout-seconds` | `30` | Timeout for Docker commands other than the image build. |
| `aimodel.specification.default-lifecycle-status` | `Active` | `lifecycleStatus` given to specifications created from MLflow. |
| `aimodel.deployment.serving-base-url` | empty | Public base of model endpoints on the default Docker host. `http://models.example.com` gives `http://models.example.com:<port>/invocations`; a template such as `https://gateway.example.com/models/{port}` replaces `{port}`. Empty: the Docker host's address (`localhost` for a local socket). Deployments on another `dockerHost` keep that host's address. An invalid value stops the application at startup. |
| `hub.webhook.enabled` | `false` | Send event notifications to subscribers (see the [integration guide](INTEGRATION_GUIDE.md#5-event-notifications-webhooks)). |
| `hub.webhook.allowed-callback-hosts` | empty | If set (comma-separated host names), only these callback hosts are accepted, including internal ones. |
| `hub.webhook.block-private-addresses` | `true` | Refuse callbacks that resolve to loopback, link-local or private addresses. Applies when `allowed-callback-hosts` is empty. |
| `server.compression.enabled` | `true` | gzip responses for clients that accept it. |

---

## 9. Troubleshooting

| Symptom | Cause and fix |
|---|---|
| Every call returns `401` | The token is expired, or its issuer doesn't match `keycloak.issuer-uri`. Get a new token from the exact URL configured (see 1.1). |
| `403` | The user lacks the realm role `ROLE_USER`. |
| A newly logged model is not in the catalog | Wait one or two minutes. Then check the API log (`docker compose logs tmf915 \| grep "MLflow sync"`). "no artifacts were found" means the files are not in MinIO yet; the API retries every 10 minutes. |
| Model stays `reserved` | `startDate` is in the future, the image is still building (minutes), or the `platform=mlflow` characteristic is missing. |
| Model goes back to `designed` | Read the last `note`. It gives a short reason and a reference, for example `Image build failed (reference 3f2c9a1b; details are in the server log)`. Find the details with `docker compose logs tmf915 \| grep 3f2c9a1b`. "model files not found": the artifacts are missing in MinIO. "No free host port": every port in `host-port-start`–`host-port-end` is taken. "Docker host not allowed": see `mlflow.docker.allowed-hosts`. |
| Model disappeared from `active` after a day | `endDate` defaults to one day after `startDate`. Set `endDate` when deploying. |
| `endpoint` not reachable | With the local Docker socket the endpoint host is `localhost`. Set `aimodel.deployment.serving-base-url` (environment variable `AIMODEL_DEPLOYMENT_SERVING_BASE_URL`) to the address clients use, and redeploy; models deployed earlier keep their old endpoint. Or deploy with a `tcp://` `dockerHost`. |
| `415 Unsupported Media Type` | Send `Content-Type: application/json`. |
| `400` on `POST /aiModel` | The response's `errors` list names the field. The most common one is a missing `software` (`[{"id": "mlflow"}]` is enough). |
| `400` when registering a webhook | The callback is not an `http`/`https` URL, or it points at an internal address; the response says which. See `hub.webhook.allowed-callback-hosts`. |
| `500` for one model or specification only | The API log says `… cannot read a stored JSON value`: one of its JSON columns in the database is corrupt. The API refuses to read it rather than show it as empty (which would let the next update erase it). Fix or clear that column in MySQL; the log names the converter and shows how the value starts. |
| Anything else | `docker compose logs -f tmf915` |
