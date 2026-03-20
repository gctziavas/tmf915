package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.mlflow.api.proto.ModelRegistry.ModelVersion;
import org.mlflow.api.proto.Service.Experiment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Periodically synchronises MLflow models with TMF 915
 * {@link AiModelSpecification} records.
 *
 * <p>On each tick the service:
 * <ol>
 *   <li>Fetches <em>all</em> logged models from MLflow via the Logged Models API
 *       (falls back to the classic Model Registry if no logged models are found)</li>
 *   <li>Groups them by unique key (experiment-name + model-id, or model-name + version)</li>
 *   <li>For every model that has no matching {@code AiModelSpecification}
 *       in the database, creates one</li>
 * </ol>
 *
 * <p>The sync is gated behind two properties:
 * <ul>
 *   <li>{@code mlflow.enabled=true} – the MLflow integration must be active</li>
 *   <li>{@code mlflow.sync.enabled=true} – sync itself must be enabled</li>
 * </ul>
 */
@Service
@ConditionalOnProperty(name = {"mlflow.enabled", "mlflow.sync.enabled"}, havingValue = "true")
public class MlflowSyncService {

    private static final Logger log = LoggerFactory.getLogger(MlflowSyncService.class);

    private final MlflowClientService mlflowClient;
    private final MlflowSpecificationService specificationService;
    private final AiModelSpecificationRepositoryService specificationRepository;

    @Value("${mlflow.sync.base-url:}")
    private String baseUrl;

    public MlflowSyncService(MlflowClientService mlflowClient,
                              MlflowSpecificationService specificationService,
                              AiModelSpecificationRepositoryService specificationRepository) {
        this.mlflowClient = mlflowClient;
        this.specificationService = specificationService;
        this.specificationRepository = specificationRepository;
    }

    /**
     * Scheduled task that synchronises MLflow models → AiModelSpecifications.
     * Tries the Logged Models API first, falls back to classic Model Registry.
     */
    @Scheduled(fixedRateString = "${mlflow.sync.interval-ms:60000}",
               initialDelayString = "${mlflow.sync.interval-ms:60000}")
    public void syncModels() {
        log.debug("MLflow sync: starting");

        // Try Logged Models API first (new MLflow 2.x)
        boolean synced = syncFromLoggedModels();

        // Fall back to classic Model Registry
        if (!synced) {
            syncFromClassicRegistry();
        }
    }

    private boolean syncFromLoggedModels() {
        List<LoggedModel> loggedModels;
        try {
            loggedModels = mlflowClient.searchAllLoggedModels();
        } catch (Exception e) {
            log.warn("MLflow sync: failed to fetch logged models, will try classic registry", e);
            return false;
        }

        if (loggedModels.isEmpty()) {
            log.debug("MLflow sync: no logged models found, will try classic registry");
            return false;
        }

        // Build experiment name cache
        Map<String, String> experimentNames = new LinkedHashMap<>();

        int created = 0;
        int skipped = 0;

        for (LoggedModel lm : loggedModels) {
            LoggedModel.LoggedModelInfo info = lm.getInfo();
            String modelId = info.getModelId();

            // Resolve experiment name (cached)
            String experimentName = experimentNames.computeIfAbsent(
                    info.getExperimentId(), this::resolveExperimentName);

            // Check if we already have a matching specification (name=experimentName, version=modelId)
            AiModelSpecification existing =
                    specificationRepository.findAiModelSpecificationByNameAndVersion(experimentName, modelId);
            if (existing != null) {
                skipped++;
                continue;
            }

            try {
                AiModelSpecificationCreate specCreate =
                        specificationService.createSpecificationFromLoggedModel(lm, experimentName, baseUrl);
                specificationRepository.createAiModelSpecification(specCreate);
                log.info("MLflow sync: created AiModelSpecification for logged model {} (experiment: {})",
                        modelId, experimentName);
                created++;
            } catch (Exception e) {
                log.warn("MLflow sync: failed to create specification for logged model {}: {}",
                        modelId, e.getMessage());
            }
        }

        log.info("MLflow sync (logged models): finished – {} created, {} already existed", created, skipped);
        return true;
    }

    private boolean syncFromClassicRegistry() {
        List<ModelVersion> allVersions;
        try {
            allVersions = mlflowClient.searchAllModelVersions();
        } catch (Exception e) {
            log.error("MLflow sync: failed to fetch model versions from classic registry", e);
            return false;
        }

        if (allVersions.isEmpty()) {
            log.debug("MLflow sync: no model versions found in classic registry either");
            return false;
        }

        // De-duplicate: keep the first occurrence per (name, version)
        Map<String, ModelVersion> uniqueVersions = new LinkedHashMap<>();
        for (ModelVersion mv : allVersions) {
            String key = mv.getName() + ":" + mv.getVersion();
            uniqueVersions.putIfAbsent(key, mv);
        }

        int created = 0;
        int skipped = 0;

        for (ModelVersion mv : uniqueVersions.values()) {
            String name = mv.getName();
            String version = mv.getVersion();

            AiModelSpecification existing =
                    specificationRepository.findAiModelSpecificationByNameAndVersion(name, version);
            if (existing != null) {
                skipped++;
                continue;
            }

            try {
                AiModelSpecificationCreate specCreate =
                        specificationService.createSpecificationFromMlflow(name, version, baseUrl);
                specificationRepository.createAiModelSpecification(specCreate);
                log.info("MLflow sync: created AiModelSpecification for {} v{}", name, version);
                created++;
            } catch (Exception e) {
                log.warn("MLflow sync: failed to create specification for {} v{}: {}",
                        name, version, e.getMessage());
            }
        }

        log.info("MLflow sync (classic registry): finished – {} created, {} already existed", created, skipped);
        return true;
    }

    private String resolveExperimentName(String experimentId) {
        Optional<org.mlflow.api.proto.Service.Experiment> experiment =
                mlflowClient.getExperiment(experimentId);
        return experiment.map(Experiment::getName).orElse("experiment-" + experimentId);
    }
}
