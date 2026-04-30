package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mlflow.api.proto.Service.Experiment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Integration tests for {@link MlflowSpecificationService} against a live MLflow instance.
 * Verifies that MLflow model metadata is correctly mapped to TMF 915 AiModelSpecificationCreate.
 *
 * Uses the Logged Models API (new MLflow 2.x) as the primary data source.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("integration")
@Tag("integration")
class MlflowSpecificationServiceIntegrationTest {

    @Autowired
    private MlflowSpecificationService specificationService;

    @Autowired
    private MlflowClientService mlflowClientService;

    // ── Logged Model → Specification ────────────────────────────────────

    @Test
    void createSpecificationFromLoggedModel_succeeds() {
        LoggedModel lm = getFirstLoggedModel();
        String experimentName = resolveExperimentName(lm.getInfo().getExperimentId());

        AiModelSpecificationCreate spec = specificationService.createSpecificationFromLoggedModel(lm, experimentName, "");

        assertNotNull(spec);
        assertEquals(experimentName, spec.getName());
        assertEquals(lm.getInfo().getModelId(), spec.getVersion());
    }

    @Test
    void createSpecificationFromLoggedModel_setsDescription() {
        LoggedModel lm = getFirstLoggedModel();
        String experimentName = resolveExperimentName(lm.getInfo().getExperimentId());

        AiModelSpecificationCreate spec = specificationService.createSpecificationFromLoggedModel(lm, experimentName, "");

        assertNotNull(spec.getDescription(), "Description should be set");
        assertFalse(spec.getDescription().isEmpty(), "Description should not be empty");
    }

    @Test
    void createSpecificationFromLoggedModel_includesLoggedModelIdCharacteristic() {
        LoggedModel lm = getFirstLoggedModel();
        String experimentName = resolveExperimentName(lm.getInfo().getExperimentId());

        AiModelSpecificationCreate spec = specificationService.createSpecificationFromLoggedModel(lm, experimentName, "");

        String loggedModelId = findCharacteristicValue(spec.getSpecCharacteristic(), "loggedModelId");
        assertNotNull(loggedModelId, "loggedModelId characteristic should be present");
        assertEquals(lm.getInfo().getModelId(), loggedModelId);
    }

    @Test
    void createSpecificationFromLoggedModel_includesExperimentIdCharacteristic() {
        LoggedModel lm = getFirstLoggedModel();
        String experimentName = resolveExperimentName(lm.getInfo().getExperimentId());

        AiModelSpecificationCreate spec = specificationService.createSpecificationFromLoggedModel(lm, experimentName, "");

        String experimentId = findCharacteristicValue(spec.getSpecCharacteristic(), "experimentId");
        assertNotNull(experimentId, "experimentId characteristic should be present");
        assertEquals(lm.getInfo().getExperimentId(), experimentId);
    }

    @Test
    void createSpecificationFromLoggedModel_includesRunIdCharacteristic() {
        LoggedModel lm = getFirstLoggedModel();
        if (lm.getInfo().getSourceRunId() == null || lm.getInfo().getSourceRunId().isEmpty()) {
            return; // skip if no runId
        }
        String experimentName = resolveExperimentName(lm.getInfo().getExperimentId());

        AiModelSpecificationCreate spec = specificationService.createSpecificationFromLoggedModel(lm, experimentName, "");

        String runId = findCharacteristicValue(spec.getSpecCharacteristic(), "runId");
        assertNotNull(runId, "runId characteristic should be present");
        assertEquals(lm.getInfo().getSourceRunId(), runId);
    }

    @Test
    void createSpecificationFromLoggedModel_includesMlflowUrlCharacteristic() {
        LoggedModel lm = getFirstLoggedModel();
        String experimentName = resolveExperimentName(lm.getInfo().getExperimentId());

        AiModelSpecificationCreate spec = specificationService.createSpecificationFromLoggedModel(lm, experimentName, "");

        String mlflowUrl = findCharacteristicValue(spec.getSpecCharacteristic(), "mlflowUrl");
        assertNotNull(mlflowUrl, "mlflowUrl characteristic should be present");
        assertTrue(mlflowUrl.contains(lm.getInfo().getModelId()), "mlflowUrl should contain the model ID");
    }

    @Test
    void createSpecificationFromLoggedModel_includesParamCharacteristics() {
        LoggedModel lm = getFirstLoggedModel();
        if (lm.getData() == null || lm.getData().getParams() == null || lm.getData().getParams().isEmpty()) {
            return; // skip if no params
        }
        String experimentName = resolveExperimentName(lm.getInfo().getExperimentId());

        AiModelSpecificationCreate spec = specificationService.createSpecificationFromLoggedModel(lm, experimentName, "");

        // Verify at least one param_ characteristic was added
        boolean hasParamChar = spec.getSpecCharacteristic().stream()
                .anyMatch(cs -> cs.getName() != null && cs.getName().startsWith("param_"));
        assertTrue(hasParamChar, "Expected at least one param_ characteristic");
    }

    // ── Classic Registry error paths ────────────────────────────────────

    @Test
    void createSpecificationFromMlflow_withNonExistingModel_throwsIOException() {
        assertThrows(IOException.class, () ->
                specificationService.createSpecificationFromMlflow("non_existent_model_xyz_999", "1", ""));
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private LoggedModel getFirstLoggedModel() {
        List<LoggedModel> models = mlflowClientService.searchAllLoggedModels();
        assertFalse(models.isEmpty(), "Need at least one logged model in MLflow");
        return models.get(0);
    }

    private String resolveExperimentName(String experimentId) {
        Optional<org.mlflow.api.proto.Service.Experiment> experiment =
                mlflowClientService.getExperiment(experimentId);
        return experiment.map(Experiment::getName).orElse("experiment-" + experimentId);
    }

    private String findCharacteristicValue(List<CharacteristicSpecification> specs, String name) {
        if (specs == null) return null;
        for (CharacteristicSpecification cs : specs) {
            if (name.equals(cs.getName())
                    && cs.getCharacteristicValueSpecification() != null
                    && !cs.getCharacteristicValueSpecification().isEmpty()) {
                Object val = cs.getCharacteristicValueSpecification().get(0).getValue();
                return val != null ? val.toString() : null;
            }
        }
        return null;
    }
}
