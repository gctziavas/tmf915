# Model artifact access: feature plan

Let clients download a catalogued model through the TMF 915 API and use it in their own code ("as a library"), instead of only calling a deployed endpoint. The design is platform-neutral: MLflow is the first platform, and Kubeflow or others can be added later without changing the API.

This plan orders the work into pull requests and lists what must be decided first. It follows the working rules of [`BUG_FIX_PLAN.md`](BUG_FIX_PLAN.md).

## Goal

A client with a token can:

1. Find a model in `GET /aiModelSpecification` and see whether it can be downloaded, in which format, and how big it is.
2. Download it with `GET /aiModelSpecification/{id}/artifacts`, without access to MLflow, MinIO or storage credentials.
3. Load it with the tool that matches the advertised format. For MLflow models that is `mlflow.pyfunc.load_model(<unzipped dir>)`.

Adding a new model platform (for example Kubeflow) means writing a small platform-specific class, not changing the endpoint, security or clients.

## Non-goals

- **No format conversion.** The API delivers artifacts as the platform stored them, labelled with their format. Turning a PyTorch model into ONNX, say, is the publisher's job (see PR 4).
- **No client SDK.** Clients use the loader of the advertised format.
- **No upload through this endpoint.** Publishing stays with the platforms (MLflow today).

## Current state (2026-11-26)

- **Discovery:** each specification synced from MLflow carries the model's MLflow identity in `specCharacteristic`: `loggedModelId`, `artifactUri` (also in `modelDataSheet`), `modelType`, `runId`, `param_*`, `metric_*`. It is written by `MlflowSpecificationService`.
- **Access today:** a client can load a model only by going around the API. It needs MLflow and MinIO network access and the shared S3 credentials, then calls `mlflow.pyfunc.load_model("models:/<loggedModelId>")`. The API's token, roles and audit do not apply.
- **Storage:** `MlflowClientService` already holds a `MinioClient` (used by the sync's `hasArtifactsByUri`).
- **TMF field:** `AiModelSpecification.attachment` (`AttachmentRefOrValue`: `href`, `attachmentType`, `mimeType`, `name`, `size`, `url`, `description`, `validFor`) exists in the TMF model and is unused.
- **Platform coupling:** the catalog sync (`MlflowSyncService`) and the specification builder (`MlflowSpecificationService`) are MLflow-only. Deployment is already platform-neutral through `PlatformDeployer`.
- **Roles:** the Keycloak realm has `ROLE_USER` and `ROLE_ADMIN`; the API checks `ROLE_USER`.

## Design

### What differs per platform, and what does not


| Concern                                                   | Platform-specific?         | Example                                                                                                                             |
| --------------------------------------------------------- | -------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| Where a model's files are, and their format               | **Yes**                    | MLflow: the logged model's`artifact_uri`, format `mlflow-model`. Kubeflow Model Registry: the model artifact's URI and format name. |
| Reading bytes from a location                             | **No: per storage scheme** | `s3://` is read the same way whoever wrote it; likewise `oci://`, `https://`.                                                       |
| Endpoint, authentication, roles, audit, streaming, limits | **No**                     | One endpoint for all platforms.                                                                                                     |
| How the client loads the files                            | **No, but advertised**     | The specification states the format; the client picks the loader.                                                                   |

### Extension points

Three small interfaces, chosen at runtime like `PlatformDeployer` is today:

```
GET /aiModelSpecification/{id}/artifacts            generic: auth, role, allow-list, zip streaming, audit
        │
        ▼
ArtifactLocator     one per platform       spec → ModelArtifact(storage URI, format, file list, total size)
  MlflowArtifactLocator        artifactUri characteristic, format "mlflow-model"
  KubeflowArtifactLocator      (PR 3) Model Registry: model artifact URI + format name
        │
        ▼
ArtifactStore       one per URI scheme     URI → list of files, and an InputStream per file
  S3ArtifactStore              s3://  (reuses the MinIO client configuration)
  OciArtifactStore, HttpArtifactStore  later, when a platform needs them

CatalogSource       one per platform       (PR 2) lists the platform's models → specifications
  MlflowCatalogSource          today's MlflowSyncService logic, behind the interface
```


| Interface         | Selected by                                                                        | Responsibility                                                                                                                                                                                                |
| ----------------- | ---------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `ArtifactLocator` | `supports(AiModelSpecification)`, on the specification's `platform` characteristic | Resolve a specification to a`ModelArtifact` (`URI location`, `String format`, `List<ArtifactFile> files`, `long totalBytes`). No byte reading.                                                                |
| `ArtifactStore`   | URI scheme                                                                         | List the files under a location and open a stream per file. No platform knowledge.                                                                                                                            |
| `CatalogSource`   | configuration (`<platform>.enabled`)                                               | Discover models on its platform and produce`AiModelSpecificationCreate` objects, including the platform-neutral fields below. The scheduling, caching and back-off now in `MlflowSyncService` become generic. |

A full platform plugin is then a `CatalogSource`, an `ArtifactLocator` and, if models should also be deployable, a `PlatformDeployer`.

### Platform-neutral fields on a specification

Written by every catalog source, read by the generic code:


| Field                                      | Value                                                                                                                                                                                                           | Purpose                                                                                         |
| ------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------- |
| `specCharacteristic` `platform`            | `mlflow`, `kubeflow`, …                                                                                                                                                                                        | Selects the locator (and matches the`platform` characteristic deployers already use).           |
| `specCharacteristic` `artifactFormat`      | see the format table                                                                                                                                                                                            | What the files are.                                                                             |
| `attachment` (one per downloadable format) | `href`: `/tmf-api/AiM/v4/aiModelSpecification/{id}/artifacts`; `attachmentType`: the format; `mimeType`: `application/zip`; `name`: `<spec name>-<version>.zip`; `size`: total bytes (`Quantity`, unit `bytes`) | The TMF-idiomatic way to advertise a downloadable artifact, so generic TMF clients can find it. |

Existing MLflow specifications have no `platform` characteristic. `MlflowArtifactLocator` therefore also accepts specifications without `platform` that have `loggedModelId` and `artifactUri`. That fallback can be removed after a backfill.

### Formats

`artifactFormat` / `attachmentType` values, with the loader a client uses:


| Format                                                         | Content                                                                | Client loads with                                                     |
| -------------------------------------------------------------- | ---------------------------------------------------------------------- | --------------------------------------------------------------------- |
| `mlflow-model`                                                 | An MLflow model directory (`MLmodel`, environment files, flavor files) | `mlflow.pyfunc.load_model(dir)`; dependencies from `requirements.txt` |
| `onnx`                                                         | An`.onnx` file                                                         | onnxruntime, in any language                                          |
| others (e.g.`pytorch`, `tensorflow-savedmodel`, `huggingface`) | As the platform reports them                                           | The framework's own loader                                            |

The table lives in `docs/INTEGRATION_GUIDE.md`. A new platform adds rows; it never changes existing meanings.

### API contract

- `GET /tmf-api/AiM/v4/aiModelSpecification/{id}/artifacts`
  - `200`, `Content-Type: application/zip`, `Content-Disposition: attachment; filename="<name>-<version>.zip"`. The zip is streamed while it is read from storage, never held in memory or on disk.
  - `404`: unknown specification, or no locator supports it. A specification that exists but has no downloadable artifact also gives `404`, with a reason.
  - `403`: missing `ROLE_USER` (decision A1).
  - `413`: larger than `aimodel.artifacts.max-download-bytes` (decision A4).
- Optional: `?format=<format>` when a specification offers several formats (PR 4).
- The JSON views (`GET` with `fields`, null exclusion, gzip) do not apply: it is a binary download. `application/zip` is not in `server.compression.mime-types`, which is right: a zip is already compressed.

### Security

1. **Storage allow-list (must have).** Clients can create specifications with `POST /aiModelSpecification`, with any `artifactUri`. Without a check, the endpoint would let any user read any object the server's S3 credentials can reach. `ArtifactStore` refuses locations outside `aimodel.artifacts.allowed-locations` (default: the MLflow bucket, `s3://mlflow/`), and the endpoint answers `404` (not `403`, so it does not confirm what exists). See also decision A5.
2. **Role and opt-out (decisions A1, A3).** Any `ROLE_USER` may download; publishers who must not share weights (licence, value) set `downloadable=false`.
3. **Executing a downloaded model runs its code.** Pickle and skops files execute on load. The API cannot make that safe; it can make who published what traceable. The endpoint logs each download at INFO: user (token subject), specification id, format and bytes. The integration guide states the risk.
4. **Zip safety.** Entry names are the file paths relative to the artifact root, normalised. The endpoint rejects any path that is absolute or contains `..`, so a malicious artifact cannot produce a zip that writes outside the client's target directory.
5. **No credentials leave the server.** Presigned URLs (decision A2) would expose MinIO to clients; streaming does not.

## Decisions (made 2026-11-26)

| #  | Decision | Options | Recommendation | **Decision** | Blocks |
| -- | -------- | ------- | -------------- | ------------ | ------ |
| A1 | Who may download | `ROLE_USER` (everyone with API access) / new realm role `ROLE_MODEL_DOWNLOAD` | New role. Weights can be valuable or licensed; metadata access should not imply them. | **`ROLE_USER`**, and the model's publisher can make a model non-downloadable (default: downloadable). No new realm role. | PR 1 |
| A2 | Transport | Stream a zip through the API / return presigned S3 URLs | Stream. No MinIO exposure, one request, works for any `ArtifactStore`. | **Stream a zip**, and bound how long a download may keep a connection open (`aimodel.artifacts.download-timeout`). | PR 1 |
| A3 | Which specifications are downloadable | All with a locator / only those marked downloadable | All with a locator, plus an opt-out characteristic `downloadable=false` set by publishers (e.g. an MLflow tag). | **As recommended** (same rule as A1). | PR 1 |
| A4 | Size limit | None / configurable limit | `aimodel.artifacts.max-download-bytes`, default 2 GB; `413` above it. | **As recommended.** | PR 1 |
| A5 | Specifications created by `POST /aiModelSpecification` | Downloadable if their location passes the allow-list / never downloadable | Allow-list only. | **As recommended.** | PR 1 |

## Pull requests

### PR 1: download endpoint for MLflow models, on the generic extension points — done, committed (`e6ec7c6`; manual check passed 2026-11-26, see PR 5)

1. `ArtifactLocator`, `ArtifactStore`, `ModelArtifact`, `ArtifactFile` in a new package `artifacts/`, plus `ArtifactService`, which selects the locator and store and enforces the allow-list, opt-out and size limit.
2. `S3ArtifactStore`: lists and streams objects with the MinIO client. Move the client construction out of `MlflowClientService` into an `S3Configuration` bean shared by both, so S3 access does not depend on MLflow classes.
3. `MlflowArtifactLocator`: `artifactUri` → location; format `mlflow-model`; the legacy fallback for specifications without `platform`.
4. Endpoint in `AiModelSpecificationApiController` (a TMF extension, documented as such), streaming with `StreamingResponseBody`, and errors through `ApiExceptionHandler`.
5. `MlflowSpecificationService` writes `platform=mlflow`, `artifactFormat=mlflow-model` and the `attachment`. Size: sum of object sizes, read when the specification is created. Existing specifications: computed on the fly by the locator; the attachment is backfilled on first read, or by a one-off job.
6. Opt-out (decisions A1, A3): no new role, the endpoint needs `ROLE_USER`. A specification with the characteristic `downloadable=false` is not downloadable (`404`) and advertises no attachment. MLflow publishers set it with the tag `downloadable=false`; anyone who can PATCH the specification can set it too.

**Tests:**

- `S3ArtifactStore` with a fake MinIO client: listing, streaming, refusing locations outside the allow-list.
- Zip writer: entry names, refusal of `..`, streaming without buffering. A 50 MB fake artifact must not grow the heap by 50 MB; assert through a counting stream.
- MockMvc: `200` with zip entries matching the files; `404` for an unknown id, for no locator, and for a location outside the allow-list; `403` without `ROLE_USER`; `413` above the limit; `downloadable=false` → `404`.
- `MlflowSpecificationServiceUnitTest`: `platform`, `artifactFormat` and `attachment` are set.
- Manual, needs MLflow and MinIO (the verification stack from PR 5 of the fix plan works): download the example model, unzip it, `mlflow.pyfunc.load_model`, predict.

**Docs:**

- `INTEGRATION_GUIDE.md`: new section "Using a model as a library", with the endpoint, formats table, security notes and a Python example.
- `MODEL_PUBLISHING_GUIDE.md`: the `downloadable=false` opt-out and what consumers will receive.
- `USER_GUIDE.md`: configuration (`aimodel.artifacts.*`) and who may download.
- README: one line under "What it does".

**Client-visible:** a new endpoint and new fields (`attachment`, `platform`, `artifactFormat`) on specifications. Nothing existing changes.

**Progress** (ticked when the item's code, tests and docs are done and `mvn clean test` passes):

- [x] 1. Extension points, `ArtifactService` (allow-list, opt-out, size limit) and the zip writer. Package `artifacts/`: `ArtifactLocator` (spec → `ArtifactLocation`: URI + format; no storage access), `ArtifactStore` (list + open, by URI scheme), `ArtifactFile`, `ModelArtifact`, `ArtifactPaths` (entry names; rejects absolute paths, drive letters and `..`, with `/` and `\\` both treated as separators), `ArtifactCharacteristics` (`platform`, `artifactFormat`, `downloadable`), `ArtifactZipWriter` (64 KB buffer, `BEST_SPEED`, stops at a deadline with `DownloadTimeoutException`, does not close the response stream) and `ArtifactService`. Difference from the design: listing is done by `ArtifactService` through the store, not by the locator, so locators never touch storage. The allow-list is checked in `ArtifactService` before any store is asked (same scheme and bucket, whole path segments, no `.`/`..`, query or fragment), so it holds for every store. Every "nothing to download" case gives the same message, except the publisher's opt-out, which says so. Storage errors are `ArtifactStorageException` (to be `503`). Tests: `ArtifactPathsTest`, `ArtifactZipWriterTest` (entries, unsafe and duplicate names, 50 MB of random data with less than 1 MB read ahead of what the client received, deadline), `ArtifactServiceTest` (opt-out, no locator, six disallowed locations never reach the store, segment matching, empty, unsafe path, storage error, size limit, `inspect`, `writeZip`). 350 tests passing.
- [x] 2. `S3Configuration` and `S3ArtifactStore`. `artifacts.S3Configuration` creates the `MinioClient` and the `S3ArtifactStore` when an endpoint and both keys are set, independent of `mlflow.enabled`; `MlflowClientService` now takes the shared client (`ObjectProvider<MinioClient>`) instead of building its own. Settings read: `mlflow.s3.endpoint.url`/`access.key`/`secret.key` (what `MLFLOW_S3_*` environment variables set, as before) **or** `mlflow.s3-endpoint-url`/`s3-access-key`/`s3-secret-key`. The second form is what `application.yml` and `application-integration.yml` use, and was silently ignored before; it now takes effect. `S3ArtifactStore` lists recursively under `<path>/` (so `m-1` does not match `m-10`), skips directory markers, and refuses keys that are unsafe or not canonical (`//`, `./`, `\\`), so a zip entry name is always the object key itself. MinIO errors become `IOException`. Tests: `S3ArtifactStoreTest` (fake MinIO client: listing, prefix, empty, unsafe keys, list/read errors, streaming, a disallowed location never reaching MinIO, and when the beans are created). 360 tests passing.
- [x] 3. `MlflowArtifactLocator` (`integrations/mlflow`). Supports `platform=mlflow` (any case), and specifications without `platform` that have `artifactUri` plus `loggedModelId` (logged models) or `modelVersion` (classic registry; the design named only logged models, but classic-registry specifications carry the same `artifactUri`). Location: `artifactUri`; format: `artifactFormat` if set, else `mlflow-model`. A missing or unparsable `artifactUri` gives the generic "no downloadable artifact". Not conditional on `mlflow.enabled`: it reads only the specification, so synced models stay downloadable with the MLflow integration off. Tests: `MlflowArtifactLocatorTest` (6). 366 tests passing.
- [x] 4. Endpoint, error mapping and download time limit. `GET /aiModelSpecification/{id}/artifacts` in `AiModelSpecificationApiController` (`ROLE_USER`; documented in OpenAPI as a TMF 915 extension), returning a `StreamingResponseBody` with `Content-Type: application/zip` and `Content-Disposition: attachment; filename="<name>-<version>.zip"` (characters outside `[A-Za-z0-9._-]` become `_`). No `produces` restriction, so clients sending `Accept: application/json` still get the zip or a readable error. `ApiExceptionHandler`: `ArtifactNotAvailableException` → `404`, `ArtifactTooLargeException` → `413`, `ArtifactStorageException` → `503` (cause logged only), all `text/plain` so an error is never labelled as a zip. Unknown id on this endpoint is also `404` `text/plain`. **Time limit (A2):** `aimodel.artifacts.download-timeout` (default `15m`). The zip writer stops at that deadline and closes the storage stream; the request's own async timeout is set to the limit plus 30 s, per request, so `spring.mvc.async.request-timeout` (also used by the MCP SSE endpoint) is unchanged. A client that stops reading is cut off by Tomcat's write timeout. Each download is logged at INFO (user, specification, format, files, bytes, duration), failures at WARN. Tests: `ArtifactDownloadHttpTest` (real server, in-memory store, 1000-byte limit): `200` with the zip entries and headers, not gzipped; legacy spec without `platform`; `404` unknown id, no locator, outside the allow-list (store never asked), `downloadable=false` (with the reason); `413`; `403` without `ROLE_USER`; `401` without token. `ArtifactDownloadTimeoutHttpTest`: an 8 s download with a 1 s limit ends in under 6 s, truncated (or `500` if nothing had been sent yet). 376 tests passing.
- [x] 5 + 6. `platform`, `artifactFormat`, `downloadable` and the `attachment` on synced specifications; backfill for existing ones. `MlflowSpecificationService` (logged models and classic registry) writes `platform=mlflow` and `artifactFormat=mlflow-model`, copies the MLflow tag `downloadable` into a `downloadable` characteristic (classic registry: the version's tag, else the registered model's), and, unless opted out, adds the attachment: `@type` `ModelArtifactAttachment` (`@baseType` `Attachment`, so it is told apart from client attachments), `attachmentType` = format, `mimeType` `application/zip`, `name` `<name>-<version>.zip`, `size` in `bytes` (TMF `Quantity.amount` is a float, so the exact byte count is also in `description`). Size comes from `ArtifactService.inspect`, which lists storage once at creation; no attachment if the model is not downloadable (outside the allow-list, no S3 settings, empty, above the limit). `href` is set by `AiModelSpecificationRepositoryService.normalize` once the id exists (new `DownloadAttachments.complete`), which also drops the attachment when `downloadable=false`. The attachment is server-owned on PATCH: put back when a PATCH replaces the attachment list without it, and a client-supplied copy is ignored and logged. (Omitted lists being wiped by every PATCH was a general bug, fixed separately: `BUG_FIX_PLAN.md`, PR 7.) **Existing specifications:** `DownloadAttachmentBackfill` runs once after startup in the background (`aimodel.artifacts.backfill-on-startup`, default `true`; skipped without S3 settings) and adds the attachment to downloadable specifications that have none, 100 per page; downloads work without it. It also restores the attachment after `downloadable=false` is removed (at the next restart). No realm change (decision A1). Tests: `MlflowSpecificationServiceUnitTest` (fields and attachment; tag opt-out without reading storage; not downloadable; classic registry with version/registered-model tags), `DownloadAttachmentsTest`, `DownloadAttachmentBackfillTest`, `AiModelSpecificationRepositoryServiceUnitTest` (href on create, PATCH keeps it and ignores client copies, `downloadable=false` removes it, `addDownloadAttachment`), `ArtifactDownloadHttpTest.backfill_advertisesTheDownloadInTheSpecification` (GET with `fields=attachment`). 391 tests passing.
- [x] Docs and `CHANGELOG.md`. `INTEGRATION_GUIDE.md`: new section 5 "Using a model as a library" (finding downloadable models, the endpoint and its statuses, time limit and checking the zip, formats table, Python example, security notes), a row in the options table, the download statuses in section 3, a "Downloads for your platform" paragraph in "Adding a serving platform", a limitation. Old sections 5–8 are now 6–9; links updated in `USER_GUIDE.md`, `README.md`, this plan, and the MCP startup warning in `McpServerConfiguration` ("section 7"). Historical references in `BUG_FIX_PLAN.md` and `PERFORMANCE_HANDOFF.md` are left as they were. `MODEL_PUBLISHING_GUIDE.md` section 3: new characteristics, "Downloads, and how to opt out" (what consumers receive, `tags={"downloadable": "false"}` on `log_model`, registry tags, PATCH for catalogued models), a troubleshooting row. `USER_GUIDE.md`: download bullet in section 3; `aimodel.artifacts.*` and the S3 settings' two spellings in section 8; three troubleshooting rows. README: one line under "What it does". `application.yml`: the `aimodel.artifacts` block; `docker-compose.yml` passes `AIMODEL_ARTIFACTS_ALLOWED_LOCATIONS`, `_MAX_DOWNLOAD_BYTES`, `_DOWNLOAD_TIMEOUT` through (`docker compose config` validates). New `CHANGELOG.md` with an "Unreleased" section. 391 tests passing.
- [x] Manual check against MLflow and MinIO. Passed on 2026-11-26 as part of the whole-feature check below (`scripts/verify_model_artifacts.py`). It found that downloads were logged as `user=null`, fixed in PR 5.

### PR 2: extract `CatalogSource` from the MLflow sync — done, committed (`a9aa3dc`)

1. `CatalogSource` interface: `List<DiscoveredModel> discover(since)`, `boolean hasArtifacts(DiscoveredModel)`, `AiModelSpecificationCreate toSpecification(DiscoveredModel)`.
2. A generic `CatalogSyncService` keeps the scheduling, positive cache, missing-artifacts back-off and incremental/full search logic from `MlflowSyncService` (fix-plan Part 3 items 2 and 3) and runs every enabled source.
3. `MlflowCatalogSource` holds the MLflow parts: the logged-models search, the classic-registry fallback and the specification mapping from `MlflowSpecificationService`.
4. Setting the platform-neutral fields (`platform`, `artifactFormat`, `attachment`) moves into the generic service, so every source gets them.

**Tests:** move `MlflowSyncServiceUnitTest`'s scenarios to `CatalogSyncService` with a fake source, and keep MLflow-specific mapping tests with `MlflowCatalogSource`.

**Docs:** `INTEGRATION_GUIDE.md` section 8 ("Adding a serving platform") becomes "Adding a model platform": catalog source, artifact locator, deployer.

**Client-visible:** none.

**Progress** (ticked when the item's code, tests and docs are done and `mvn clean test` passes):

- [x] 1. `CatalogSource`, `DiscoveredModel` and the generic `CatalogSyncService`, tested with a fake source (the scenarios of `MlflowSyncServiceUnitTest`). New package `catalog/`. `CatalogSource<T>`: `platform()`, `discover(Long createdAfterMs)` (`null` = full search; a source may return more than asked), `hasArtifacts`, `toSpecification`. `DiscoveredModel<T>(key, name, version, createdAtMs, details)`: `key` for the caches, `name`/`version` for the "already in the catalog" check, `createdAtMs` for ordering and the watermark (`0` = unknown: such models never start incremental searches, which keeps MLflow's classic registry searched in full every tick, as before), `details` opaque to the sync. `CatalogSyncService` keeps per source: the synced-key cache, the 10-minute missing-artifacts back-off, the watermark (5-minute overlap), the last full search and whether incremental searches work (one failure turns them off and a full search runs in the same tick). Check order per model as before: cache, database, back-off, artifacts, create. Models are processed newest first (sorted by `createdAtMs`; `MlflowSyncService` reversed the list MLflow returned). One failing source or model does not stop the others. Schedule: `aimodel.catalog.sync.interval-ms` and `full-interval-ms`, defaulting to the `mlflow.sync.*` values, so existing settings keep working. With no sources (MLflow sync off) it does nothing. Tests: `CatalogSyncServiceTest` (12, with `FakeCatalogSource`): the five incremental-search scenarios and the cache/back-off scenario of `MlflowSyncServiceUnitTest`, plus models without creation time, ordering, conversion, a failing model, a failing source with per-source state, no sources. `MlflowSyncService` still runs until item 2 (no `CatalogSource` bean exists yet). 406 tests passing.
- [x] 2. `MlflowCatalogSource` (logged models, incremental search, classic-registry fallback); `MlflowSyncService` removed. `integrations/mlflow/MlflowCatalogSource implements CatalogSource<MlflowModel>` (sealed: `LoggedModelEntry`, `RegistryVersion`), active with `mlflow.enabled` and `mlflow.sync.enabled` as the old service was. Full search: logged models (key `<experiment>:<modelId>`, name = experiment, version = model id, experiment names looked up once per search); if none, or the search fails, the classic registry (key `registry:<name>:<version>`, first per name/version, creation time `0`). Incremental search: logged models created after the time, never the registry, and MLflow's rejection propagates so the sync switches to full searches. `toSpecification` calls `MlflowSpecificationService` (kept as the MLflow mapper rather than merged into the source). **Fix found on the way:** the registry artifact check prefixed `s3://mlflow/` to the version's `source` even when it was already a full URI (`s3://mlflow/s3://…`), so such versions never looked stored; a `source` with a scheme is now used as it is, a relative one still gets the prefix. `MlflowSyncService` and `MlflowSyncServiceUnitTest` deleted; `MlflowSyncServiceIntegrationTest` renamed `MlflowCatalogSyncIntegrationTest` and runs `CatalogSyncService.syncAll()`. Tests: `MlflowCatalogSourceTest` (9: logged-model keys and names, registry fallback and de-duplication, fallback on a failed search, incremental search without registry, rejected filter, artifact URIs for both kinds, specification mapping), `CatalogSyncWiringTest` (the Spring context hands the MLflow source to the generic sync), `MlflowDisabledStartupTest` (no `CatalogSource` without MLflow). 410 tests passing.
- [x] 3. `platform`, `artifactFormat` and the download `attachment` set by `CatalogSyncService` for every source, no longer by `MlflowSpecificationService`. `CatalogSource` gains `artifactFormat()` (the platform's default; `mlflow-model` for MLflow). Before storing, `CatalogSyncService.addPlatformNeutralFields` adds `platform` and `artifactFormat` unless the source set them, and the attachment unless one exists. Whether to add it is decided by `ArtifactService.describe` on the specification as it will be stored (opt-out, locator, allow-list, storage, size limit): the same path as the download endpoint, so the catalog cannot advertise a download that would fail. (`MlflowSpecificationService` used to call `inspect` on `artifactUri` directly.) `MlflowSpecificationService` no longer depends on `ArtifactService`; it still turns the MLflow tag `downloadable` into the characteristic, the only MLflow-specific part. New helpers `ArtifactCharacteristics.of` and `addIfAbsent`. Tests: `CatalogSyncServiceTest` (+3: fields and attachment, not downloadable, source's own values kept), `CatalogSyncAttachmentTest` (real locator, allow-list and an in-memory store: attachment with size; none when opted out, outside the allow-list or empty), `MlflowSpecificationServiceUnitTest` reduced to the MLflow mapping (lifecycle status, `artifactUri`, the tag on logged models and in the registry, no neutral fields), `MlflowCatalogSourceTest` (format). 414 tests passing.
- [x] 4. Settings and docs. Schedule: `aimodel.catalog.sync.interval-ms` / `full-interval-ms`, falling back to `mlflow.sync.interval-ms` / `full-interval-ms` (not added to `application.yml` with values, which would hide an `MLFLOW_SYNC_*` override; a comment there says so). `mlflow.sync.enabled` and `mlflow.sync.base-url` stay MLflow settings. `INTEGRATION_GUIDE.md`: section 8 is now "Adding a model platform": a table of the three pieces, 8.1 catalog source (contract of each method, what the sync does), 8.2 downloads (locator and store), 8.3 deployer (its snippet corrected: `deploy` returns the characteristics, plus `validateCharacteristics`); the options table names the three plugin interfaces. `USER_GUIDE.md`: the two schedule settings, troubleshooting greps `Catalog sync`. `AI_MODEL_DEPLOYMENT_FLOW.md` section 1 and `puml/model_repository_block.puml` describe the new classes (the old text also claimed a `statObject` check; it lists at most one object). `CHANGELOG.md`: the settings and log prefix under Changed, the registry URI fix under Fixed. 414 tests passing.

### PR 3: Kubeflow — not planned (future work, decided 2026-11-26)

Kept as a sketch for whoever picks it up; `INTEGRATION_GUIDE.md` section 8 describes the three pieces a platform needs.

1. **Confirm before starting:** the Kubeflow Model Registry REST API version in use, how it exposes a model version's artifact URI and format, and which storage schemes it uses in your installation (S3 is covered by PR 1; `oci://` needs an `OciArtifactStore`; `pvc://` is not reachable from the API and would need a different approach).
2. `KubeflowCatalogSource` and `KubeflowArtifactLocator`.
3. An extra `ArtifactStore` only if step 1 shows a scheme other than `s3://`.
4. Deployment: a `KubeflowDeployer` (KServe `InferenceService`) is a separate decision; downloads do not depend on it.

**Tests:** against a recorded Model Registry response (fake HTTP), and a manual check against a real installation.

### PR 4: portable formats (guidance first, code if needed) — done, committed (`21f565c`)

1. `MODEL_PUBLISHING_GUIDE.md`: how to also log an ONNX version of a model, and why (any language, no framework dependency).
2. If publishers do that, the catalog source adds a second `attachment` (`onnx`), and the endpoint serves `?format=onnx`.

**Convention (decided while implementing, 2026-11-26).** The publisher adds the ONNX file when logging the model: `log_model(..., extra_files=["model.onnx"])`. MLflow stores it as `extra_files/model.onnx` in the model directory (checked with MLflow 3.10.1). A specification whose model has that file offers a second format, `onnx`: its own attachment, downloaded with `?format=onnx` as a zip holding just `model.onnx`. Without `format`, the download is the specification's primary format (`artifactFormat`, `mlflow-model`), as in PR 1, and its attachment `href` is unchanged. Why this convention: one call at log time, no extra MLflow API, no race with the sync (the file exists before the catalog entry is created), and the MLflow model itself stays complete. Limits: the ONNX file must be self-contained (no external data files), and models catalogued earlier get the `onnx` attachment only if they had no download attachment at all (the startup backfill skips specifications that already advertise one).

**Progress** (ticked when the item's code, tests and docs are done and `mvn clean test` passes):

- [x] 1. Several formats per specification in the generic download code: a locator returns one location per format (the first is the primary), a location can name the files it consists of, `ArtifactService` resolves a requested format and describes every available one. `ArtifactLocator.locate` returns `List<ArtifactLocation>`; `ArtifactLocation(uri, format, paths)` (empty `paths`: everything under `uri`; otherwise exactly those files, all required). `ArtifactService.resolve(spec, format)` (`null`: primary); an unknown format, or one whose files are missing, is `404` "has no download in format X". `describeAll(spec)` replaces `describe` and returns every available format, primary first; the size limit applies per format, so a large MLflow model can still offer a small ONNX file. The sync and the backfill add one attachment per available format (`addDownloadAttachments`). `MlflowArtifactLocator` still returns only the primary here. Tests: `ArtifactServiceTest` (+8: primary by default, named format with only its files, primary by name, unknown and missing format, `describeAll` order and opt-out, multi-file location, size limit per format, zip of a named format); existing tests adapted. 422 tests passing.
- [x] 2. `MlflowArtifactLocator` offers `onnx` from `extra_files/model.onnx`; endpoint `?format=`; one attachment per format (sync, backfill, PATCH, `href` per format). Locator: primary location as before, plus `(<artifactUri>/extra_files, onnx, [model.onnx])` unless the primary format is already `onnx`; the zip entry is `model.onnx`. Endpoint: optional `format` (`[A-Za-z0-9._-]{1,64}`, else `400`, so it is safe in logs and file names); file name `<name>-<version>-<format>.zip` when a format is named. **Attachment links now always name their format** (`…/artifacts?format=mlflow-model`, `…?format=onnx`), and `DownloadAttachments.complete` sets each attachment's `href` and `name` (`<name>-<version>-<format>.zip`) from the specification on every store and read. So existing attachments from PR 1 show `?format=mlflow-model` too; the PR 1 link without `format` keeps working (primary format). This avoids having to work out which attachment is the primary one, which a specification without `artifactFormat` cannot tell. PATCH keeps every format's attachment. Tests: `MlflowArtifactLocatorTest` (+2), `DownloadAttachmentsTest` (+3), `AiModelSpecificationRepositoryServiceUnitTest` (+1), `ArtifactDownloadHttpTest` (+5: `?format=onnx` gives only `model.onnx`, named primary format, format not offered or not stored, malformed format, opted out; the backfill test now sees both attachments), `CatalogSyncAttachmentTest` (+1); five assertions updated for the new link and name. `InMemoryArtifactStore` now lists recursively, like S3. 434 tests passing.
- [x] 3. Docs: publishing an ONNX version (`MODEL_PUBLISHING_GUIDE.md`), using it (`INTEGRATION_GUIDE.md` section 5), `CHANGELOG.md`. `MODEL_PUBLISHING_GUIDE.md` section 3, "Also offer an ONNX version": why (any language, no framework, smaller), how (`skl2onnx.to_onnx` for the section 2 example, a prediction check with ONNX Runtime, `extra_files=["model.onnx"]`), which flavors accept `extra_files` (checked against MLflow 3.10.1: `sklearn`, `pytorch`, `xgboost`, `lightgbm`, `catboost`, `statsmodels`, `onnx`; not `pyfunc`, `transformers`), converters, limits (self-contained file, per-format size limit, only at logging time); a troubleshooting row. `INTEGRATION_GUIDE.md` section 5: two attachments in the example, `?format=` with `400`/`404`, per-format size limit, `onnx` row in the formats table, new 5.5 "Run the ONNX version" (ONNX Runtime), security note that ONNX is a graph, not code (5.6); section 8.2 and the limitations describe several formats. `USER_GUIDE.md`: `?format=onnx`, a `404` reason. `CHANGELOG.md`: ONNX downloads (Added), attachment `href`/`name` naming the format (Changed). Verification step 5 added below. Attachment descriptions say "1 file". The `skl2onnx` snippet was verified in the manual check (2026-11-26, PR 5) with `skl2onnx` 1.20.0 and `onnxruntime` 1.23.2: it runs as written, and ONNX Runtime predicts like the pipeline. 434 tests passing.

### PR 5: findings of the manual check — done, uncommitted

The manual check (2026-11-26, below) passed, but every download was logged with `user=null`. **Cause:** Keycloak 25 and later put the `sub` claim in a client scope named `basic`, which realms imported from older exports lack. `docker/keycloak/openslice-realm.json` has none, so the demo realm's tokens (Keycloak 26.0.8) have no `sub`. Spring names the authentication after `sub`, so `Authentication.getName()` is `null`. Nothing else in the API uses the name. Decided 2026-11-26: fix both the server and the demo realm (A1's "no realm change" was about roles, and still holds).

**Progress** (ticked when the item's code, tests and docs are done and `mvn clean test` passes):

- [x] 1. Server: name the authentication after `sub`, or after `preferred_username` when a token has no `sub`, so downloads are attributed with any Keycloak realm. `SecurityConfiguration.jwtAuthenticationConverter()` (now static, package-private) wraps Spring's `JwtAuthenticationConverter` and only replaces the name, keeping its authorities (Spring Security 7 adds `FACTOR_BEARER` to every bearer token). A token with neither claim keeps a `null` name, as before. Tests, both failing before the change: `SecurityConfigurationTest` (5: subject, fallback, neither, realm roles, same authorities with and without a subject) and `ArtifactDownloadHttpTest.tokenWithoutSubject_downloadIsLoggedWithTheUsername` (the audit line says `user=alice`). `INTEGRATION_GUIDE.md` section 5.2 says which claim is logged. 440 tests passing.
- [x] 2. Demo realm: add the `basic` client scope (the `sub` mapper) as a default scope of `osapiWebClientId`, so demo tokens carry `sub`. On inspection, Keycloak 26 creates `basic` on import and makes it a realm default, but `osapiWebClientId` lists its default scopes explicitly (`web-origins`, `roles`, `profile`, `email`), so the client never got it. The fix is one line: `basic` added to that list; no scope definition needed. Checked on the verification stack: after re-importing the edited realm, the token has `sub`, the same roles, and a download is logged as `user=<sub>`. `CHANGELOG.md` (Fixed) says how to add the scope to an existing stack, whose realm Keycloak does not re-import. 440 tests passing.
- [x] 2a. Integration tests against the verification stack (client, specification and catalog-sync; the deployer and lifecycle tests, which build Docker images, not run): 22 of 23 passed. `MlflowCatalogSyncIntegrationTest.syncModels_createsSpecificationsFromLoggedModels` failed whenever it ran after `syncModels_isIdempotent` (JUnit's default order): the shared sync remembers published models and, as documented (`MODEL_PUBLISHING_GUIDE.md` section 6), does not recreate a deleted specification before a restart, but the test deletes them and expects a sync to recreate them. It passed alone. Pre-existing: `MlflowSyncService` had the same cache and the test the same two methods before PR 2. Fix: `@DirtiesContext(methodMode = BEFORE_METHOD)` on that test, so it starts with a sync that has not run; no production change. Then 23 of 23 passed in the same order. 440 unit tests passing.
- [x] 3. Record the manual check: PR 1's item ticked, PR 4's "Not verified" note replaced, outcome under "Verification of the whole feature". `CHANGELOG.md` has the item 1 and 2 fix (Fixed); the check itself changes nothing for clients.

### PR 6: optional extras, and the known follow-ups — done, uncommitted

Decided 2026-11-27: do the optional items before merging to `main`, and note the follow-ups without fixing them.

1. Note the follow-ups found while building the feature, in this plan and where operators or clients would look.
2. Postman: requests to find a downloadable specification and download it in each format.
3. MCP: the specification tools also say in which formats a model can be downloaded, with size and link. Metadata only: the MCP endpoints are unauthenticated, so they must not stream files, which would bypass `ROLE_USER`.

**Progress** (ticked when the item's code, tests and docs are done and `mvn clean test` passes):

- [x] 1. Follow-ups noted: section "Follow-ups (not planned)" below, four items with their impact and a possible fix. For users: `USER_GUIDE.md` section 8 has a `spring.task.execution.pool.core-size` row (downloads share that pool, and more wait for a thread). `INTEGRATION_GUIDE.md` 5.1 says that specifications catalogued before PR 4 advertise only `mlflow-model`, and that `?format=onnx` works for them. `MODEL_PUBLISHING_GUIDE.md` section 3 says that removing `downloadable=false` brings the download back at once, but the attachment only at the next restart. The float size was already documented (5.1). Docs only.
- [x] 2. Postman requests, added at the end so the deploy flow (3 to 7) keeps its numbers. **8** lists specifications with `fields=name,version,attachment`, picks the first one with a `ModelArtifactAttachment`, and stores `downloadSpecificationId` and `downloadFormats` (new collection variables). **8.1** downloads the primary format and checks `200`, the zip `Content-Type` and `Content-Disposition`. **8.2** downloads `?format=onnx`: it expects `200` and a zip when the specification offers `onnx`, and otherwise `404` with "has no download in format onnx". The scripts were run in Node against sample responses (six cases, including ones that must fail); not run in Postman or newman, which is not installed. `INTEGRATION_GUIDE.md` 5.2 points to them. JSON formatting kept (2-space indent).
- [x] 3. MCP download formats. `AiCatalogMcpTools.AiModelSpecificationSummary` gains `downloads`, a list of `DownloadSummary(format, href, sizeBytes)` built from the specification's `ModelArtifactAttachment`s (`DownloadAttachments.findAll`), in their order (primary first). A client's own attachments are left out, and the list is empty if the model cannot be downloaded. `href` is the API link the repository sets on every read; `sizeBytes` is `size.amount` rounded (approximate above 16 MiB, follow-up 4). The tools serve metadata only, never files, because MCP is unauthenticated; their descriptions mention the formats. Tests: `AiCatalogMcpToolsUnitTest` (+2: formats with link and size, a client attachment left out; not downloadable gives an empty list), which failed to compile before. `INTEGRATION_GUIDE.md` section 7: table and a paragraph on `downloads`; `CHANGELOG.md` (Added). 442 tests passing.

## Verification of the whole feature

On the verification stack (fix plan, PR 5 outcome), with the example model from `MODEL_PUBLISHING_GUIDE.md`:

1. `GET /aiModelSpecification?fields=name,version,attachment` shows the attachment, with size and format.
2. `GET /aiModelSpecification/{id}/artifacts` with a `ROLE_USER` token returns a zip of that size; without the role, `403`; with `downloadable=false`, `404`.
3. In a clean Python environment: unzip, `pip install -r requirements.txt`, `mlflow.pyfunc.load_model(dir).predict(…)` returns the same predictions as the deployed endpoint for the same input.
4. A specification created by POST with `artifactUri` outside the allow-list returns `404` on download.
5. Log the example model again with `extra_files=["model.onnx"]` (`MODEL_PUBLISHING_GUIDE.md` section 3): its specification has a second attachment (`onnx`), and `…/artifacts?format=onnx` gives `model.onnx`, which ONNX Runtime runs with the same predictions.

**Outcome (2026-11-26): passed, 44 of 44 checks.** Automated as `scripts/verify_model_artifacts.py` (configured by environment variables; see its docstring). Run against a throwaway copy of `HEAD` (`21f565c`) as Compose project `tmf915verify`. It used the pinned images (MySQL 8.0.46, Keycloak 26.0.8 with `KC_DB=dev-file` instead of MySQL to skip its 25-minute first start, MLflow server 3.18.0, the MinIO mirrors) on remapped host ports. The client side was MLflow 3.10.1, scikit-learn 1.7.2, `skl2onnx` 1.20.0 and `onnxruntime` 1.23.2 on Python 3.10.

- Step 1: catalogued 40 s after logging, with attachments `mlflow-model` (259,846 bytes) and `onnx` (80,115 bytes, the size of `model.onnx`). Each `href` ends in `?format=`. Each size equals the unzipped files' total, in `size.amount` and in `description`. The model logged with `downloadable=false` has no attachment.
- Step 2: `200` zips with `Content-Disposition: attachment`. Without `format`, the download has the same files as `mlflow-model`, and its file name has no format. No token gives `401`. A user without `ROLE_USER` (created for the check) gets `403`. `downloadable=false` gives `404` naming it. `format=pytorch` gives `404`, `format=a%2Fb` gives `400`.
- Step 3: `mlflow.pyfunc.load_model` predicts like the trained pipeline, both in the client environment and in an empty virtual environment after `pip install -r requirements.txt` from the zip. Nothing was deployed on the stack, so the comparison is with the pipeline in the same process instead of a deployed endpoint.
- Step 4: a POSTed specification with `artifactUri` `s3://other/x` and `platform` `mlflow` has no attachment. It gets `404`, and the text does not reveal the location.
- Step 5: the guide's `skl2onnx` snippet runs as written. The `onnx` zip holds only `model.onnx`, which ONNX Runtime runs with the pipeline's predictions.
- Finding: the server's download log said `user=null`, because the demo realm's tokens had no `sub`. Fixed in PR 5.
- Integration tests (client, specification, catalog sync) against the same stack: 23 of 23 after the PR 5 item 2a test fix.

## Follow-ups (not planned)

Found while building the feature; noted 2026-11-27, not fixed. Each is described for users where they would look.

1. **No limit on concurrent downloads.** Downloads stream on Spring's shared task pool (`applicationTaskExecutor`, 8 threads by default, unbounded queue), which the startup backfill also uses. A ninth download waits for a thread instead of being refused. A slow client holds a thread for up to `aimodel.artifacts.download-timeout`. Possible fix: a dedicated executor for downloads with a bounded queue, answering `503` with `Retry-After` when full. Documented in `USER_GUIDE.md` section 8 (`spring.task.execution.pool.core-size`).
2. **The startup backfill does not add `onnx` to specifications that already advertise a download.** It only fills specifications with no download attachment at all, so those catalogued between PR 1 and PR 4 keep only `mlflow-model` even when their model has `extra_files/model.onnx`. `?format=onnx` still works for them, because downloads ask the locator, not the attachments. Possible fix: the backfill compares each specification's attachments with `ArtifactService.describeAll` and adds the missing formats. Documented in `INTEGRATION_GUIDE.md` section 5.1.
3. **Removing `downloadable=false` restores the attachment only at the next restart.** The download works again immediately; the attachment is put back by the startup backfill. Possible fix: when a PATCH changes `downloadable` from `false`, describe the specification again and add its attachments. Documented in `MODEL_PUBLISHING_GUIDE.md` section 3.
4. **`size.amount` is a float.** TMF's `Quantity.amount` is a 32-bit float, so sizes above 16 MiB (2^24 bytes) are rounded, by up to 128 bytes at 2 GB. The exact byte count is in the attachment's `description`. Not fixable without leaving the TMF schema. Already documented in `INTEGRATION_GUIDE.md` section 5.1.

## Working rules

As in [`BUG_FIX_PLAN.md`](BUG_FIX_PLAN.md):

- Write the failing test first where an item says how to verify it.
- `mvn clean test` green; the count only goes up.
- Update the listed docs in the same PR, and mark the PR done here with a short outcome.
- Collect client-visible changes in `CHANGELOG.md` under "Unreleased".
- No commits or branches by the assistant; the owner commits.
