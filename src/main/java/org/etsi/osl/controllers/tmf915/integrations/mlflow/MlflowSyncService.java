package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.mlflow.api.proto.ModelRegistry.ModelVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Periodically synchronises MLflow registered models with TMF 915
 * {@link AiModelSpecification} records.
 *
 * <p>On each tick the service:
 * <ol>
 *   <li>Fetches <em>all</em> model versions from MLflow
 *       ({@link MlflowClientService#searchAllModelVersions()})</li>
 *   <li>Groups them by (model-name, version)</li>
 *   <li>For every pair that has no matching {@code AiModelSpecification}
 *       in the database, creates one via
 *       {@link MlflowSpecificationService#createSpecificationFromMlflow}</li>
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
     * Runs at the fixed rate configured by {@code mlflow.sync.interval-ms}.
     */
    @Scheduled(fixedRateString = "${mlflow.sync.interval-ms:60000}",
               initialDelayString = "${mlflow.sync.interval-ms:60000}")
    public void syncModels() {
        log.debug("MLflow sync: starting");

        List<ModelVersion> allVersions;
        try {
            allVersions = mlflowClient.searchAllModelVersions();
        } catch (Exception e) {
            log.error("MLflow sync: failed to fetch model versions", e);
            return;
        }

        if (allVersions.isEmpty()) {
            log.debug("MLflow sync: no model versions found in MLflow");
            return;
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

            // Check if we already have a matching specification
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

        log.info("MLflow sync: finished – {} created, {} already existed", created, skipped);
    }
}
