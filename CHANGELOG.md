# Changelog

Client-visible changes. Releases are not tagged yet; everything below is on `main` or on its way there.

## Unreleased

### Added

- **Model download** (TMF 915 extension): `GET /tmf-api/AiM/v4/aiModelSpecification/{id}/artifacts` streams a model's files as a zip, so clients can load the model in their own code without access to MLflow or MinIO. Requires `ROLE_USER`. Answers `404` (unknown id, not downloadable, or opted out), `413` (above `aimodel.artifacts.max-download-bytes`, 2 GB by default) and `503` (storage unavailable), each with a plain-text reason. Downloads are cut off after `aimodel.artifacts.download-timeout` (15 minutes by default). See `docs/INTEGRATION_GUIDE.md` section 5.
- New fields on specifications synced from MLflow: the characteristics `platform` (`mlflow`) and `artifactFormat` (`mlflow-model`), and, for downloadable models, an `attachment` of `@type` `ModelArtifactAttachment` with the download `href`, format, `mimeType` and `size`. Existing specifications get the attachment after the next start.
- Publishers can opt a model out of downloads with the MLflow tag `downloadable=false`, or by setting the characteristic `downloadable` to `false` on the specification.

### Changed

- `INTEGRATION_GUIDE.md`: sections 5 to 8 are now 6 to 9 (new section 5, "Using a model as a library").
- The `mlflow.s3-endpoint-url`, `s3-access-key` and `s3-secret-key` settings in `application.yml` now take effect; before, only the `MLFLOW_S3_*` environment variables did. Environment variables still win.
- A `PATCH` of a specification can no longer remove or replace its download attachment.
