# Bug fix plan

This plan orders the open items in [`PERFORMANCE_HANDOFF.md`](PERFORMANCE_HANDOFF.md) Parts 4 and 5 into pull requests. Each item's problem, fix and test are described there; this file only says **in what order**, **grouped how**, and **what must be decided first**. Item numbers (4.x, 5.x) refer to the handoff.

Baseline (2026-11-26): `main` at `16c1380`, `mvn clean test` 224 tests, all passing.

## Decisions needed before starting

| # | Decision | Options | Recommendation | Decided (2026-11-26) | Blocks |
|---|---|---|---|---|---|
| D1 | Server-owned characteristics in client input (5.1) | Drop silently / reject with `400` | Drop and log. Clients that read a model and PATCH it back keep working. | **Drop and log** | PR 1 |
| D2 | MCP authentication (4.3) | (a) bearer token, (b) network-level only, (c) open by design | (a). The repository is going public, and "open by design" is hard to defend for an endpoint that lists deployed models. | **(b) network-level only** | PR 3 |
| D3 | Host-port allocation (5.3) | Retry only on port conflicts / let Docker choose (`-p 0:…`) | Retry only on port conflicts. It keeps the 3000–7000 range that firewalls may depend on. | **Retry only on port conflicts** | PR 2 |
| D4 | `serving-base-url` (4.6) | Implement / delete | Implement: it fixes unreachable `endpoint` values. | **Implement** (as a template with optional `{port}`, applied to the default Docker host) | PR 5 |
| D6 | `PATCH state=reserved` on an `active` model (5.13) | Reject with `400` / redeploy (undeploy, then deploy) | Reject. The client can set `inactive` first. | **Reject** | PR 1 |
| D5 | Model endpoints without auth (4.8) | Gateway / inference proxy / document | Document for the first public release, and plan the proxy as a feature after release. Out of scope here. | open | — |

## Milestone A: safe to expose (must be done before open-sourcing)

### PR 1: lock down deployment inputs (5.1 steps 1 and 4, 5.13 step 1) — done, uncommitted

Outcome: see the handoff, 5.1 and 5.13. `mvn clean test`: 245 tests, all passing.

- Filter server-owned characteristics out of `AiModelCreate`/`AiModelUpdate` in `AiModelLifecycleService`, before the specification enrichment runs.
- Add the state-transition table (5.13): clients can request only `reserved` and `inactive`, and only from states where that makes sense. Per D6, `reserved` on an `active` model is rejected.
- Add `mlflow.docker.allowed-hosts`; validate `dockerHost` at create/PATCH time.
- Small and self-contained, so it can merge first. It closes the "delete any container" hole for new writes.
- Client-visible: `dockerHost` outside the allow-list now returns `400`; PATCHing `containerId` etc. has no effect; `PATCH state=active`/`terminated` and other unsupported transitions return `400`.

### PR 2: rewrite the Docker command layer (5.4, 5.2, 5.3, 5.1 step 2, 5.13 step 2, 5.10) — done, committed (`4b83a01`)

Outcome: see "PR 2 outcome" in the handoff, Part 5. `mvn clean test`: 272 tests, all passing. Parts were also checked against a real Docker daemon; the manual check below is still open.

All of these live in `MlflowDeploymentService` and `MlflowModelService`. One PR avoids rewriting the same methods three times.

1. **5.4 first.** Introduce a `ProcessRunner` interface with one implementation that drains output and enforces timeouts. Every later test in this PR uses a fake runner, so no Docker is needed in `mvn test`.
2. **5.2:** container names include the model id; remove only containers this attempt created; `deriveUniqueName` no longer repeats names.
3. **5.3:** retry the next port only on a port conflict (per D3).
4. **5.1 step 2:** `--label tmf915.aiModelId=<id>` on `docker run`; check the label before stop/rm.
5. **5.13 step 2:** deploy results replace `containerId`, `hostPort`, `endpoint` and the other server-owned characteristics instead of appending; undeploy clears them.
6. **5.10:** remove an image only when no other live model uses it; drop `-f`.

Client-visible: container names change (`<name>-<id8>` instead of `<name>-v<version>`). Models deployed before the upgrade have no label, so the label check in step 4 would refuse to undeploy them. Handle this in the PR: when the container has no label, read its name (`docker inspect --format '{{.Name}}' <containerId>`) and accept it only if the name equals what the old scheme would have produced for this model (`generateContainerName` before this PR). Log a warning. Remove the fallback in a later release.

Manual check after merge, needs Docker and MLflow: deploy two models with the same name; both stay up; undeploy one; the other still answers.

### PR 3: remaining exposure (4.3 per D2, 5.7, 5.12, 5.11 part) — done, committed (`66aa1de`)

Outcome: see "PR 3 outcome" in the handoff, Part 5. `mvn clean test`: 288 tests, all passing.

- 4.3, per D2 (network-level only): keep `/mcp/**` and `/sse/**` in `permitAll`. Document in `INTEGRATION_GUIDE.md` section 6, `USER_GUIDE.md` and the README that the API port must not be reachable from untrusted networks, or the MCP paths must be blocked at a reverse proxy. Give an example rule. Add a startup log line saying the MCP endpoints are unauthenticated.
- 5.7: validate webhook callbacks.
- 5.12: classified failure notes; full detail only in server logs.
- 5.11: delete `permit-all`/`origins`/`spring-addons` from `application.yml`; review `include-message: always`.

## Milestone B: deployments are reliable

### PR 4: split the deployment transaction (5.6, 5.5, 4.1) — done, committed (`71c3994`)

Outcome: see "PR 4 outcome" in the handoff, Part 5. `mvn clean test`: 298 tests, all passing.

These three change the same two methods, `DeploymentScheduler.executeDeployment` and `executeUndeploy`.

1. **5.6:** short read transaction → deploy outside any transaction → short write transaction. Catch and log `Throwable` in every scheduled task.
2. **5.5:** in the write step, if the model was deleted or is no longer `reserved`, undeploy the new container instead of writing `active`.
3. **4.1:** publish `AiModelDomainEvent(STATE_CHANGE)` after the write transaction commits: `active`, `designed` or `terminated`.

This also fixes the connection-pool problem noted in Part 2: a build no longer holds a Hikari connection for 10 minutes.

Client-visible: webhook subscribers start receiving `active`, `designed` and `terminated` events from background work.

## Milestone C: works on a fresh machine

### PR 5: first-run experience (4.2, 4.7, 5.8, 5.11 rest, 4.6 per D4, 4.5) — done, uncommitted (acceptance run on a clean VM still open)

Done one item at a time; each ticked item is complete (code, tests, docs), and the tree builds after each.

- [x] 5.8: starts with `mlflow.enabled=false`; tests use it. `MlflowSpecificationService` is now `@ConditionalOnProperty(mlflow.enabled)`, like the other MLflow beans. Tests default to `mlflow.enabled=false` (`src/test/resources/application.properties`); `AiModelListHttpTest` enables it, because the `dockerHost` checks live in `MlflowDeployer`. New `MlflowDisabledStartupTest`. Found and fixed: the application's explicit `@ComponentScan` has no `TypeExcludeFilter`, so a nested `@TestConfiguration` (the fake deployer in `DeploymentSchedulerSpringTest`) leaked into every test context; it is now a plain class added with `@Import`. Docs: `USER_GUIDE.md` section 8. 299 tests passing.
- [x] 4.5: `lifecycleStatus` on synced specifications. `MlflowSpecificationService` sets `aimodel.specification.default-lifecycle-status` (default `Active`) on logged-model and classic-registry specifications. New `MlflowSpecificationServiceUnitTest`. Docs: `MODEL_PUBLISHING_GUIDE.md` section 3, `USER_GUIDE.md` sections 3 and 8. **Release note, backfill for existing rows** (column name from `DATABASE_SCHEMA.md`): `UPDATE aim915_aimspec SET lifecycle_status = 'Active' WHERE lifecycle_status IS NULL;` 300 tests passing.
- [x] 4.6 (D4: implement): `serving-base-url`. `MlflowDeploymentService.endpointFor`: when set and the container is on the default Docker host, the endpoint is the base plus `:<port>`, or the template with `{port}` replaced; another `dockerHost` keeps its address. `validateServingBaseUrl` (`@PostConstruct`) stops startup on a value without http(s) scheme and host, or with a port or path but no `{port}`. Tests in `MlflowDeploymentServiceUnitTest`. Docs: `application.yml` comment, `USER_GUIDE.md` sections 5, 8, troubleshooting; `INTEGRATION_GUIDE.md` 4.3. Already passed through by `docker-compose.yml` (`AIMODEL_DEPLOYMENT_SERVING_BASE_URL`). Existing models keep their endpoint until redeployed. 305 tests passing.
- [x] 4.2 + 5.11 rest: issuer and MLflow/MinIO defaults; lab address removed. `docker-compose.yml`: both issuer variables are `${KEYCLOAK_ISSUER_URI:-http://localhost:8080/auth/realms/openslice}`; MLflow and MinIO default to `http://mlflow:5000` and `http://minio:9000` (the `full` profile), so the quick start needs no exports. `application.yml` docker profile: the same service names. `application-integration.yml`: `TMF915_IT_MLFLOW_URI`, `TMF915_IT_S3_URL`, `TMF915_IT_DOCKER_HOST` with `localhost` defaults; `MlflowDeploymentServiceIntegrationTest` no longer asserts the lab address. Test data in `CallbackUrlPolicyTest` and `DeploymentSchedulerTest` uses `172.16.0.10`. `docker compose config` validates and shows the new defaults. No `172.16.100.94` left outside `docs/PERFORMANCE_HANDOFF.md`, `docs/BUG_FIX_PLAN.md` and `docs/OPEN_SOURCE_ROADMAP.md` (history). Docs: `USER_GUIDE.md` 1.1, 1.2; `README.md` quick start. Not covered: Swagger UI's OAuth URLs (`springdoc.oAuthFlow.*`) still point at `localhost:8080`, so signing in to Swagger from another machine needs `SPRINGDOC_OAUTHFLOW_AUTHORIZATIONURL` and `SPRINGDOC_OAUTHFLOW_TOKENURL`.
- [x] 4.7: `minio-init` creates the `mlflow` bucket. One-shot `minio/mc` service in the `full` profile: waits until `mc alias set` succeeds, then `mc mb --ignore-existing local/mlflow`; `mlflow` depends on it with `condition: service_completed_successfully`. Checked with `docker compose config` (valid; `minio-init` in the `full` profile). **Not run against a live MinIO:** a throwaway check on an isolated network was interrupted (its container and network were removed). Verify on the clean-VM acceptance run below. Docs: `USER_GUIDE.md` 1 (service table) and 1.2 (manual bucket step removed).
- [x] Handoff "PR 5 outcome". `mvn clean test`: 305 tests, all passing.


- 4.2: `KEYCLOAK_ISSUER_URI` and the MLflow/MinIO URLs default to `localhost` or Compose service names.
- 4.7: `minio-init` service creates the `mlflow` bucket.
- 5.8: the app starts with `mlflow.enabled=false`; tests use it.
- 5.11: replace the `172.16.100.94` lab address everywhere (`application.yml` docker profile, `docker-compose.yml`, `src/test/resources/application-integration.yml`, `MlflowDeploymentServiceIntegrationTest`, `docs/USER_GUIDE.md`).
- 4.6: implement `serving-base-url`.
- 4.5: set `lifecycleStatus` on synced specifications, plus the backfill SQL in the release notes.

Acceptance: on a machine that has never run the project, `git clone` → `docker compose --profile full up -d --build` → get a token → `GET /aiModel` returns `200`, and a sample model logged to the local MLflow appears in the catalog within a minute. Do this run on a clean VM; it is also the open-source "does the quick start work" test.

## Milestone D: correct errors and data

### PR 6: errors and data integrity (4.4, 5.9)

- Optional first step: upgrade springdoc to the 3.x line, which supports Spring Boot 4. That allows a single `@ControllerAdvice` for error mapping instead of per-controller handlers. `ResponseCompressionTest` guards `/v3/api-docs`. If the upgrade causes trouble, do 4.4 with per-controller `@ExceptionHandler`s as the handoff describes.
- 4.4: `404` for unknown ids on PATCH/DELETE for both resources (client-visible: `aiModel` changes from `400` to `404`).
- 5.9: converters fail loudly instead of returning empty values.

## Working rules for every PR

- Write the failing test first where the item says how to verify it, as in Part 1.
- `mvn clean test` green; the count only goes up from 224.
- Update the docs listed under the item in the same PR. Mark the item done in the handoff with a short outcome, as Parts 1 to 3 do.
- Collect client-visible changes in `CHANGELOG.md` (created in the open-source roadmap) under "Unreleased".

## Summary

| Milestone | PRs | Items | Needed for open source |
|---|---|---|---|
| A: safe to expose | 1, 2, 3 | 5.1, 5.13, 5.2, 5.3, 5.4, 5.10, 4.3, 5.7, 5.12, 5.11 (part) | Yes |
| B: reliable deployments | 4 | 5.6, 5.5, 4.1 | Strongly recommended |
| C: fresh machine | 5 | 4.2, 4.7, 5.8, 5.11, 4.6, 4.5 | Yes: the quick start must work |
| D: errors and data | 6 | 4.4, 5.9 (+ springdoc) | No |
