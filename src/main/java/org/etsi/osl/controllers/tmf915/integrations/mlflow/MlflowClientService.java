package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.mlflow.api.proto.ModelRegistry.ModelVersion;
import org.mlflow.api.proto.ModelRegistry.RegisteredModel;
import org.mlflow.api.proto.Service.Experiment;
import org.mlflow.api.proto.Service.Run;
import org.mlflow.tracking.MlflowClient;
import org.mlflow.tracking.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@ConditionalOnProperty(name = "mlflow.enabled", havingValue = "true")
public class MlflowClientService {

    private static final Logger log = LoggerFactory.getLogger(MlflowClientService.class);

    private final MlflowClient mlflowClient;

    public MlflowClientService(MlflowClient mlflowClient) {
        this.mlflowClient = mlflowClient;
    }

    // ── Registered Models ───────────────────────────────────────────────

    public Optional<RegisteredModel> getRegisteredModel(String name) {
        log.debug("Fetching registered model: {}", name);
        try {
            return Optional.ofNullable(mlflowClient.getRegisteredModel(name));
        } catch (Exception e) {
            log.warn("Registered model not found: {}", name, e);
            return Optional.empty();
        }
    }

    // ── Model Versions ──────────────────────────────────────────────────

    public List<ModelVersion> searchModelVersions(String filter) {
        log.debug("Searching model versions with filter: {}", filter);
        List<ModelVersion> allVersions = new ArrayList<>();
        Page<ModelVersion> page = mlflowClient.searchModelVersions(filter);
        page.getItems().forEach(allVersions::add);
        while (page.hasNextPage()) {
            page = page.getNextPage();
            page.getItems().forEach(allVersions::add);
        }
        return allVersions;
    }

    /**
     * Returns all model versions across all registered models.
     */
    public List<ModelVersion> searchAllModelVersions() {
        log.debug("Fetching all model versions");
        List<ModelVersion> allVersions = new ArrayList<>();
        Page<ModelVersion> page = mlflowClient.searchModelVersions();
        page.getItems().forEach(allVersions::add);
        while (page.hasNextPage()) {
            page = page.getNextPage();
            page.getItems().forEach(allVersions::add);
        }
        return allVersions;
    }

    public List<ModelVersion> getLatestVersions(String modelName) {
        log.debug("Fetching latest versions for model: {}", modelName);
        return mlflowClient.getLatestVersions(modelName);
    }

    public Optional<ModelVersion> getModelVersion(String modelName, String version) {
        log.debug("Fetching model version: {} v{}", modelName, version);
        try {
            return Optional.ofNullable(mlflowClient.getModelVersion(modelName, version));
        } catch (Exception e) {
            log.warn("Model version not found: {} v{}", modelName, version, e);
            return Optional.empty();
        }
    }

    // ── Experiments ─────────────────────────────────────────────────────

    public Optional<Experiment> getExperiment(String experimentId) {
        log.debug("Fetching experiment: {}", experimentId);
        try {
            return Optional.ofNullable(mlflowClient.getExperiment(experimentId));
        } catch (Exception e) {
            log.warn("Experiment not found: {}", experimentId, e);
            return Optional.empty();
        }
    }

    // ── Runs ────────────────────────────────────────────────────────────

    public Optional<Run> getRun(String runId) {
        log.debug("Fetching run: {}", runId);
        try {
            return Optional.ofNullable(mlflowClient.getRun(runId));
        } catch (Exception e) {
            log.warn("Run not found: {}", runId, e);
            return Optional.empty();
        }
    }

    // ── Artifacts ───────────────────────────────────────────────────────

    public String getModelVersionDownloadUri(String modelName, String version) {
        log.debug("Fetching download URI for model: {} v{}", modelName, version);
        return mlflowClient.getModelVersionDownloadUri(modelName, version);
    }
}
