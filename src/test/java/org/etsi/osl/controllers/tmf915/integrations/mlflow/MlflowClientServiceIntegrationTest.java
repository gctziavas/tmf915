package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.junit.jupiter.api.Tag;
import org.mlflow.api.proto.ModelRegistry.ModelVersion;
import org.mlflow.api.proto.ModelRegistry.RegisteredModel;
import org.mlflow.api.proto.Service.Run;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Integration tests for {@link MlflowClientService} against a live MLflow instance.
 * Requires a running MLflow server at the URI configured in application-integration.yml.
 *
 * Tests cover both the new Logged Models API and the classic Model Registry API.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("integration")
@Tag("integration")
class MlflowClientServiceIntegrationTest {

    @Autowired
    private MlflowClientService mlflowClientService;

    // ── Logged Models API ───────────────────────────────────────────────

    @Test
    void listExperimentIds_returnsNonEmptyList() {
        List<String> ids = mlflowClientService.listExperimentIds();
        assertNotNull(ids, "listExperimentIds should never return null");
        assertFalse(ids.isEmpty(), "Expected at least one experiment in the MLflow server");
    }

    @Test
    void searchAllLoggedModels_returnsNonNullList() {
        List<LoggedModel> models = mlflowClientService.searchAllLoggedModels();
        assertNotNull(models, "searchAllLoggedModels should never return null");
    }

    @Test
    void searchAllLoggedModels_containsAtLeastOneModel() {
        List<LoggedModel> models = mlflowClientService.searchAllLoggedModels();
        assertFalse(models.isEmpty(),
                "Expected at least one logged model in the MLflow server");
    }

    @Test
    void searchAllLoggedModels_modelsHaveInfoFields() {
        List<LoggedModel> models = mlflowClientService.searchAllLoggedModels();
        assumeFalse(models.isEmpty(), "No logged models available — skipping");

        for (LoggedModel lm : models) {
            assertNotNull(lm.getInfo(), "Logged model info should not be null");
            assertNotNull(lm.getInfo().getModelId(), "Model ID should not be null");
            assertFalse(lm.getInfo().getModelId().isEmpty(), "Model ID should not be empty");
            assertNotNull(lm.getInfo().getExperimentId(), "Experiment ID should not be null");
        }
    }

    @Test
    void searchLoggedModels_withSpecificExperiment_returnsModels() {
        List<String> ids = mlflowClientService.listExperimentIds();
        assumeFalse(ids.isEmpty(), "No experiments — skipping");

        List<LoggedModel> models = mlflowClientService.searchLoggedModels(ids);
        assertNotNull(models);
        // If there are logged models at all, this should find them
        List<LoggedModel> all = mlflowClientService.searchAllLoggedModels();
        assertEquals(all.size(), models.size(),
                "searchLoggedModels with all experiment IDs should match searchAllLoggedModels");
    }

    @Test
    void getLoggedModel_withExistingModel_returnsPresent() {
        List<LoggedModel> models = mlflowClientService.searchAllLoggedModels();
        assumeFalse(models.isEmpty(), "No logged models — skipping");

        String modelId = models.get(0).getInfo().getModelId();
        Optional<LoggedModel> result = mlflowClientService.getLoggedModel(modelId);
        assertTrue(result.isPresent(), "Expected logged model '" + modelId + "' to exist");
        assertEquals(modelId, result.get().getInfo().getModelId());
    }

    @Test
    void getLoggedModel_withNonExistingModel_returnsEmpty() {
        Optional<LoggedModel> result = mlflowClientService.getLoggedModel("m-nonexistent999999");
        assertTrue(result.isEmpty(), "Expected empty Optional for non-existent logged model");
    }

    @Test
    void searchAllLoggedModels_modelsHaveData() {
        List<LoggedModel> models = mlflowClientService.searchAllLoggedModels();
        assumeFalse(models.isEmpty(), "No logged models — skipping");

        // At least some models should have data (params/metrics)
        boolean anyHasData = models.stream()
                .anyMatch(lm -> lm.getData() != null
                        && ((lm.getData().getParams() != null && !lm.getData().getParams().isEmpty())
                            || (lm.getData().getMetrics() != null && !lm.getData().getMetrics().isEmpty())));
        assertTrue(anyHasData, "Expected at least some logged models to have params or metrics");
    }

    @Test
    void getRun_withRunFromLoggedModel_returnsPresent() {
        List<LoggedModel> models = mlflowClientService.searchAllLoggedModels();
        Optional<LoggedModel> withRun = models.stream()
                .filter(lm -> lm.getInfo().getSourceRunId() != null
                        && !lm.getInfo().getSourceRunId().isEmpty())
                .findFirst();

        if (withRun.isEmpty()) {
            return; // no logged models with run ID — skip quietly
        }

        String runId = withRun.get().getInfo().getSourceRunId();
        Optional<Run> run = mlflowClientService.getRun(runId);
        assertTrue(run.isPresent(), "Expected run '" + runId + "' to exist");
    }

    // ── Classic Model Registry API ──────────────────────────────────────

    @Test
    void searchAllModelVersions_returnsNonNullList() {
        List<ModelVersion> versions = mlflowClientService.searchAllModelVersions();
        assertNotNull(versions, "searchAllModelVersions should never return null");
    }

    @Test
    void getRegisteredModel_withNonExistingModel_returnsEmpty() {
        Optional<RegisteredModel> model = mlflowClientService.getRegisteredModel("non_existent_model_xyz_999");
        assertTrue(model.isEmpty(), "Expected empty Optional for non-existent model");
    }

    @Test
    void getModelVersion_withNonExistingVersion_returnsEmpty() {
        Optional<ModelVersion> result = mlflowClientService.getModelVersion("non_existent_model_xyz_999", "999");
        assertTrue(result.isEmpty(), "Expected empty Optional for non-existent version");
    }

    @Test
    void getRun_withNonExistingRun_returnsEmpty() {
        Optional<Run> run = mlflowClientService.getRun("0" + "0".repeat(31));
        assertTrue(run.isEmpty(), "Expected empty Optional for non-existent run");
    }
}
