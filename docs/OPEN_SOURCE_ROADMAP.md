# Open-source roadmap

What has to happen before this repository is made public, and what should follow soon after. Findings are from a review of the code, configuration, docs and full git history on 2026-11-26.

Stages, in order:

1. Decide (licence, ownership, hosting, history). Nothing else should be published before these are settled.
2. Blockers: required before the repository becomes public.
3. First-release quality: should be in place for the first tagged release.
4. After launch.

## 1. Decisions (owner, possibly with the university and the 6G-XCEL consortium)

### 1.1 Who owns the copyright

The first 14 commits are authored with a University of Patras address, the later ones with a personal address. The README credits the 6G-XCEL project (Horizon Europe grant 101139194). Work done as university staff or under a grant is often owned by the institution, or subject to the consortium agreement.

**Action:** confirm with the university (and the project coordinator if the consortium agreement covers software) who holds the copyright and whether open-sourcing needs approval. Horizon Europe generally encourages open access, but the consortium agreement decides. Put the answer in the copyright line of `LICENSE`/`NOTICE`.

### 1.2 Licence

The repository is **GPL-3.0** today (`LICENSE`, README). Things to weigh:

- The code is in the `org.etsi.osl` package and the README presents it as an implementation for ETSI OpenSlice. OpenSlice's own repositories are, to my knowledge, **Apache-2.0**; confirm this. If the goal is for OpenSlice to adopt or ship this component, GPL-3.0 will likely block that, and an upstream contribution would follow ETSI's contribution process and licence.
- GPL-3.0 is a valid choice for a standalone project if strong copyleft is intended.
- Whatever the choice: add a short SPDX header (`// SPDX-License-Identifier: …`) to source files, and a `NOTICE` file with the copyright holder and the funding acknowledgement.

**Recommendation:** decide based on 1.4. If OpenSlice is the destination, use Apache-2.0. If not, GPL-3.0 can stay.

### 1.3 Third-party content and names

- `.openapi-generator/TMF915_AI_Management_API_v4.0.0.oas.yaml` and `src/main/resources/openapi.yaml` are TM Forum's specification, and `api/` and `model/` are generated from it. Check the TM Forum licence terms for redistributing the spec and derived code, and add an attribution line to `NOTICE`.
- Names: "TM Forum", "TMF915", "ETSI", "OpenSlice". Use them descriptively ("an implementation of TMF915 for OpenSlice"). Without ETSI's agreement, do not suggest the project is an official ETSI or TM Forum deliverable. The `org.etsi.osl` package name and `groupId` suggest it is. Keep them only if ETSI/OpenSlice is fine with that; otherwise rename (for example `io.github.<owner>.tmf915`) before the first release, while nobody depends on the names.
- EU emblem and SNS logo in the README: required by the grant's acknowledgement rules and allowed in that role. Keep the wording as it is.

### 1.4 Where it lives

The current remote is a personal GitHub repository. Options: make it public as is, move it to a project or university organisation, or contribute it to OpenSlice on ETSI's GitLab. This affects 1.2 and the CI choice (GitHub Actions vs GitLab CI).

### 1.5 Git history: keep or start fresh

Full-history scan results:

| Found | Where | Risk |
|---|---|---|
| Private lab address `172.16.100.94` | 65 occurrences across history; still in the current tree | Low: not routable from the internet, but it reveals internal network layout |
| Default development credentials (`letmein`, `minioadmin`, `kcadmin`, `admin`/`openslice`, `clientsecret: "secret"`) | Compose, `application.yml`, realm, deleted test scripts | Low: well-known defaults, not real secrets. Fine if documented as development-only (see 2.3) |
| University email in author metadata | Commits from 2026-03-18 and 2026-03-19 | Owner's choice |
| Deleted files: `presentation.pdf`, `presentation.md`, `deploy_test*.py`, old guides | History only | Review `presentation.pdf` before publishing; it was not inspected here |
| JWTs, cloud keys, GitHub tokens, private keys | None found | — |

The scan was a pattern search, not a dedicated tool. **Action:** run `gitleaks detect --log-opts="--all"` (or `trufflehog git file://.`) once more before going public.

**Recommendation:** keep the history; nothing found requires a rewrite. If `presentation.pdf` turns out to be confidential, or the owner wants the university address gone, rewrite with `git filter-repo` before the first public push, never after. Delete the stale branches (`feat/hub-api`, `feature/lifecycle-deployment`, `feature/unit-tests`) locally and on the remote.

## 2. Blockers: required before the repository becomes public

### 2.1 Fix the security bugs

[`BUG_FIX_PLAN.md`](BUG_FIX_PLAN.md) Milestone A (PRs 1 to 3). The most important is handoff item 5.1: today any authenticated user can make the server stop and remove any container on the Docker host. Publishing the code makes such holes easy to find. Fix them first, or at the very least publish only after PR 1 and PR 2.

### 2.2 Remove lab-specific values

Replace `172.16.100.94` in `docker-compose.yml`, the `docker` profile in `src/main/resources/application.yml`, `src/test/resources/application-integration.yml`, `MlflowDeploymentServiceIntegrationTest.java`, `docs/USER_GUIDE.md` and `docs/PERFORMANCE_HANDOFF.md`. Use Compose service names or `localhost` (handoff 4.2 and 5.11).

### 2.3 Safe defaults in Docker Compose

- Every service publishes on all interfaces: MySQL `3306`, Keycloak `8080`, MinIO `9010`/`9011`, MLflow `5000`. Bind them to `127.0.0.1` by default (`"127.0.0.1:3306:3306"`). Someone who runs the quick start on a cloud VM otherwise exposes a database with password `letmein` to the internet.
- Move credentials into a committed `.env.example`, read them in Compose with `${VAR:-default}`, and add `.env` to `.gitignore`.
- Use a dedicated MySQL user for the application instead of `root`.
- State clearly in the README and Compose file that the defaults are for local development only.

### 2.4 Document the security model (`SECURITY.md`)

- How to report a vulnerability privately (GitHub private vulnerability reporting, or an email address).
- The API container mounts the Docker socket, which is root-equivalent on the host. Run it only on a dedicated Docker host.
- Model endpoints are unauthenticated (handoff 4.8) and published on host ports 3000 to 7000.
- What `ROLE_USER` allows; MCP access (per decision D2 in the fix plan).

### 2.5 The quick start works on a fresh machine

[`BUG_FIX_PLAN.md`](BUG_FIX_PLAN.md) Milestone C, including the clean-VM acceptance run. The first thing most visitors do is run the quick start; if it fails, most of them leave.

### 2.6 Licence files in place

`LICENSE` per 1.2, `NOTICE` per 1.1 and 1.3, SPDX headers, the README licence section, and a `<licenses>` block in `pom.xml`.

### 2.7 Repository hygiene

- `.gitignore` contains `.github`, which would silently ignore CI workflows and issue templates. Remove that line. Also fix `.ds_store` to `.DS_Store`, and add `.vscode/`, `.env`, `*.class`.
- Internal working documents: `docs/PERFORMANCE_HANDOFF.md` is a brief for a coding agent, not user documentation. Before launch, move its open items into issues (see 3.3), and either move the file to `docs/dev/` as a design log or delete it. Do the same with `BUG_FIX_PLAN.md` once its milestones are done or are issues.

## 3. First-release quality

### 3.1 Contributor files

- `CONTRIBUTING.md`: prerequisites (JDK 17+, Maven, Docker), `mvn test`, how to run the integration tests, "do not regenerate `api/` and `model/`", the VS Code build-into-`target/` pitfall from the handoff, and commit/PR conventions. Decide on DCO (`Signed-off-by`) or a CLA. A DCO is lighter; ETSI uses its own process if the code goes upstream.
- `CODE_OF_CONDUCT.md` (Contributor Covenant).
- `.github/ISSUE_TEMPLATE/` (bug, feature) and `PULL_REQUEST_TEMPLATE.md`.
- `CHANGELOG.md` (Keep a Changelog format), starting with the client-visible changes listed in the fix plan.

### 3.2 Continuous integration

- GitHub Actions workflow: `mvn -B verify` on every push and PR (JDK 17 and 21), and a `docker build` of the image.
- Dependabot (or Renovate) for Maven, Docker and Actions.
- CodeQL, or another static analysis tool, for Java.
- Optional: run the integration tests against Testcontainers (MySQL, MinIO, MLflow) instead of the lab hosts. Until then, document them as manual.

### 3.3 Turn known issues into public issues

Open one issue per remaining handoff item (and 4.8 as a feature request). Label them `good first issue` where they are small (4.5, 5.11, 4.7). Visible known issues build more trust than a clean-looking tracker.

### 3.4 Versioning and releases

- `pom.xml` version `4.0.0` is the TMF API version, not the project's version. Use semantic versioning for the project (for example `0.1.0`), and record the TMF915 API version separately in the README.
- Tag `v0.1.0` and create a GitHub release with the changelog.
- Publish the container image to GHCR from a release workflow.

### 3.5 Dependencies and build

- ~~springdoc 2.6.0 is not fully compatible with Spring Framework 7 (handoff Part 2 notes). Move to the 3.x line.~~ Done in fix plan PR 6: springdoc 3.0.3.
- JaCoCo 0.8.11 prints errors on recent JDKs. Upgrade it.
- Remove the explicit `<version>` on `spring-boot-starter-data-jpa` (managed by the parent).
- Pin the image tags of `minio/minio` and `ghcr.io/mlflow/mlflow` in Compose; both float to `latest` today.
- The Dockerfile builds and runs on JDK 21 while `pom.xml` targets 17. Either is fine; state the supported versions in the README.
- The runtime image runs as root. Add a non-root user where the Docker socket's group permissions allow it, or document why root is needed.

### 3.6 README for outsiders

- Status line: research prototype, maturity, what is and is not implemented (already partly there).
- Architecture diagram (exists) and a link to `SECURITY.md`.
- Badges: CI, licence, latest release.
- A "Run without MLflow" section once 5.8 is fixed.

## 4. After launch

- Answer issues and PRs within a stated time (for example a week). Say so in `CONTRIBUTING.md`, or say that the project is maintained on a best-effort basis.
- Inference proxy with token checks (handoff 4.8), Helm chart, more serving platforms via `PlatformDeployer`.
- Announce on the OpenSlice and 6G-XCEL channels.

## Checklist

| | Item | Section |
|---|---|---|
| ☐ | Copyright holder confirmed | 1.1 |
| ☐ | Licence chosen; `LICENSE`, `NOTICE`, SPDX headers | 1.2, 2.6 |
| ☐ | TM Forum spec terms checked; naming/package decided | 1.3 |
| ☐ | Hosting decided | 1.4 |
| ☐ | `presentation.pdf` in history reviewed; gitleaks run on full history | 1.5 |
| ☐ | Stale branches deleted | 1.5 |
| ☐ | Fix-plan Milestone A merged (security) | 2.1 |
| ☐ | Lab IP removed everywhere | 2.2 |
| ☐ | Compose ports bound to localhost; `.env.example`; non-root DB user | 2.3 |
| ☐ | `SECURITY.md` | 2.4 |
| ☐ | Fix-plan Milestone C merged; clean-VM quick start passes | 2.5 |
| ☐ | `.gitignore` fixed; internal docs moved or removed | 2.7 |
| ☐ | `CONTRIBUTING.md`, `CODE_OF_CONDUCT.md`, templates, `CHANGELOG.md` | 3.1 |
| ☐ | CI green on `main` | 3.2 |
| ☐ | Known issues filed | 3.3 |
| ☐ | Version set; `v0.1.0` tagged | 3.4 |
