# TMF 915 AI Management API: Integration & Handover Guide

This document is designed for third-party platforms, orchestrators, and external developers who need to integrate with the **TMF 915 AI Management API**. It provides the necessary contracts, authentication mechanisms, and expected lifecycles for managing AI Models programmatically.

---

## 1. Overview & API Contracts

The TMF 915 API provides a standardized interface (based on TMForum specifications) to manage the lifecycle of Artificial Intelligence models, linking AI metadata with physical deployments (via MLflow and Docker).

* **OpenAPI Specification / Swagger UI:** Available at `http://<TMF915_HOST>:13082/swagger-ui.html`
* **Base Path:** `/tmf-api/AiM/v4`
* **Core Entities:**
  * `AiModelSpecification`: The catalog of available, deployable AI models (synced automatically from MLflow).
  * `AiModel`: The actual runtime instance of a model. Creating/patching this entity triggers physical deployment and scaling.
  * `Hub`: The event subscription endpoint for receiving callbacks when an AI Model changes state.

---

## 2. Authentication (Keycloak OAuth 2.0)

If the platform is configured securely using the default `docker` profile, all endpoints are protected by Keycloak JWT validation.

### Requesting an Access Token

You must obtain a JWT from the Keycloak server before making API requests.

**Endpoint:** `POST http://<KEYCLOAK_HOST>:8080/auth/realms/openslice/protocol/openid-connect/token`

**Standard Client Configuration:**

* **Grant Type:** `password` (or `client_credentials` depending on IAM setup)
* **Client ID:** `osapiWebClientId`
* **Username / Password:** Provided by your Keycloak administrator.

**Example Request:**

```bash
curl -X POST "http://<KEYCLOAK_HOST>:8080/auth/realms/openslice/protocol/openid-connect/token" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "grant_type=password&client_id=osapiWebClientId&username=<USER>&password=<PASS>"
```

**Usage:**
Include the resulting `access_token` in all TMF 915 API requests as a header:
`Authorization: Bearer <access_token>`

---

## 3. Core Integration Lifecycle

External platforms should follow this lifecycle to deploy and consume AI models.

### Step A: Discover Available Models

Before deploying, the integrating platform should query the available `AiModelSpecification` entities. These represent ready-to-deploy models tracked in MLflow.

```http
GET /tmf-api/AiM/v4/aiModelSpecification
Accept: application/json;charset=utf-8
```

*Save the `id` of the desired specification for the next step.*

### Step B: Trigger a Model Deployment

To deploy a model into a containerized serving endpoint, the integrating platform must `POST` a new `AiModel` referencing the specification's `id`.

**Critical Field:** Setting `"state": "reserved"` informs the deployment scheduler to begin provisioning the runtime container.

```http
POST /tmf-api/AiM/v4/aiModel
Content-Type: application/json;charset=utf-8

{
  "name": "integration-model-instance",
  "state": "reserved",
  "aiModelSpecification": {
    "id": "<SPECIFICATION_ID>"
  },
  "serviceCharacteristic": [
    {
      "name": "platform",
      "valueType": "string",
      "value": "mlflow"
    },
    {
      "name": "dockerHost",
      "valueType": "string",
      "value": "unix:///var/run/docker.sock" 
    }
  ]
}
```

*(Note: `dockerHost` dictates where the engine attempts to spin up the ML container. Ensure the TMF915 host has access to this socket/TCP port).*

### Step C: Polling for the Inference Endpoint

Because container provisioning (downloading artifacts, building images) takes time, the returned `AiModel` will initially be in `reserved` or `inProgress` states.

**Note on Webhooks (Hub API):** While the TMForum OpenAPI specification defines a `/hub` endpoint for event subscriptions, Webhook event broadcasting is *currently not implemented* in this iteration of the TMF915 API. Integrating platforms must actively poll the resource.

Poll the individual instance:

```http
GET /tmf-api/AiM/v4/aiModel/<AI_MODEL_ID>
```

**Success State (`active`):**
Once running, the API patches the model. You will find the inference URL and expected payload schema inside the `serviceCharacteristic` array (look for keys named `endpoint` and `inferencePayloadExample`).

**Failure State (`designed`):**
If deployment fails (e.g., MinIO network timeout), the engine safely rolls back the state to `designed`. Look inside the `.note` array for a detailed stack trace/error message.

### Step D: Teardown / Undeploy

To preserve resources, the external platform can stop the AI container by patching the model state to `inactive`.

```http
PATCH /tmf-api/AiM/v4/aiModel/<AI_MODEL_ID>
Content-Type: application/json

{
  "state": "inactive"
}
```

*To completely delete it, issue a `DELETE /tmf-api/AiM/v4/aiModel/<AI_MODEL_ID>` request.*

---

## 4. Common Integration Pitfalls & Troubleshooting

* **Missing JSON Headers:** Ensure `Accept: application/json;charset=utf-8` and `Content-Type: application/json;charset=utf-8` are present. The Spring API might reject requests with `415 Unsupported Media Type` otherwise.
* **401 Unauthorized:** Your access token may have expired. Refresh your token against Keycloak.
* **Containers Not Spinning Up:** Ensure the `dockerHost` variable sent in the `POST /aiModel` payload is reachable by the TMF915 engine.
* **Missing Models in Specification List:** The MLflow synchronization happens asynchronously. If a model was literally just added to MLflow, it may take a few moments to sync into the TMF915 catalog.
