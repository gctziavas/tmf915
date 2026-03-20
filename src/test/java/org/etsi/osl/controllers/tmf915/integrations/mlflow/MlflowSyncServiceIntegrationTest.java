package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mlflow.api.proto.Service.Experiment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Integration tests for {@link MlflowSyncService} against a live MLflow instance.
 * Verifies that MLflow models are correctly synced as TMF 915 AiModelSpecifications.
 *
 * The sync service tries logged models first, then falls back to classic registry.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "mlflow.sync.enabled=true",
                "mlflow.sync.interval-ms=9999999"   // prevent auto-trigger
        })
@ActiveProfiles("integration")
@Tag("integration")
class MlflowSyncServiceIntegrationTest {

    @Autowired
    private MlflowSyncService syncService;

    @Autowired
    private MlflowClientService mlflowClientService;

    @Autowired
    private AiModelSpecificationRepositoryService specRepo;

    private final List<String> createdSpecIds = new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (String id : createdSpecIds) {
            try {
                specRepo.deleteAiModelSpecification(id);
            } catch (Exception ignored) {
            }
        }
        createdSpecIds.clear();
    }

    @Test
    void syncModels_createsSpecificationsFromLoggedModels() {
        List<LoggedModel> loggedModels = mlflowClientService.searchAllLoggedModels();
        assumeFalse(loggedModels.isEmpty(), "No logged models — skipping");

        // Build experiment name cache so we know what specs to expect
        Map<String, String> experimentNames = new LinkedHashMap<>();
        for (LoggedModel lm : loggedModels) {
            String expId = lm.getInfo().getExperimentId();
            experimentNames.computeIfAbsent(expId, this::resolveExperimentName);
        }

        // Delete existing specs that match logged models so sync will recreate them
        for (LoggedModel lm : loggedModels) {
            String expName = experimentNames.get(lm.getInfo().getExperimentId());
            String modelId = lm.getInfo().getModelId();
            AiModelSpecification existing = specRepo.findAiModelSpecificationByNameAndVersion(expName, modelId);
            if (existing != null) {
                specRepo.deleteAiModelSpecification(existing.getId());
            }
        }

        // Track existing specs to know which are new
        List<AiModelSpecification> before = specRepo.findAllAiModelSpecifications();

        // Run sync
        syncService.syncModels();

        // Verify new specs were created
        List<AiModelSpecification> after = specRepo.findAllAiModelSpecifications();
        assertTrue(after.size() > before.size(),
                "Expected new specs after sync, before=" + before.size() + " after=" + after.size());

        // Track created specs for cleanup
        for (AiModelSpecification spec : after) {
            if (before.stream().noneMatch(b -> b.getId().equals(spec.getId()))) {
                createdSpecIds.add(spec.getId());
            }
        }

        // Verify at least some logged models have matching specs
        int matched = 0;
        for (LoggedModel lm : loggedModels) {
            String expName = experimentNames.get(lm.getInfo().getExperimentId());
            AiModelSpecification spec = specRepo.findAiModelSpecificationByNameAndVersion(
                    expName, lm.getInfo().getModelId());
            if (spec != null) matched++;
        }
        assertTrue(matched > 0, "Expected at least some logged models to have matching specs");
    }

    @Test
    void syncModels_isIdempotent() {
        // Run sync twice
        syncService.syncModels();
        List<AiModelSpecification> afterFirst = specRepo.findAllAiModelSpecifications();

        syncService.syncModels();
        List<AiModelSpecification> afterSecond = specRepo.findAllAiModelSpecifications();

        assertEquals(afterFirst.size(), afterSecond.size(),
                "Running sync twice should not create duplicates");

        // Track for cleanup
        for (AiModelSpecification spec : afterSecond) {
            createdSpecIds.add(spec.getId());
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private String resolveExperimentName(String experimentId) {
        Optional<org.mlflow.api.proto.Service.Experiment> experiment =
                mlflowClientService.getExperiment(experimentId);
        return experiment.map(Experiment::getName).orElse("experiment-" + experimentId);
    }
}
