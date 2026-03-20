package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.deployment.PlatformDeployer;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mlflow.api.proto.Service.Experiment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Integration tests for {@link MlflowDeployer} — the PlatformDeployer implementation.
 * Tests the full deploy lifecycle: build image → start container → verify ACTIVE → undeploy.
 *
 * <p>Uses the Logged Models API to obtain models for deployment.
 *
 * <p>WARNING: These tests build Docker images and run containers on the remote Docker host.
 * They may take several minutes and consume resources.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("integration")
@Tag("integration")
class MlflowDeployerIntegrationTest {

    @Autowired
    private MlflowDeployer mlflowDeployer;

    @Autowired
    private MlflowModelService mlflowModelService;

    @Autowired
    private MlflowClientService mlflowClientService;

    @Autowired
    private MlflowDeploymentService deploymentService;

    @Autowired
    private AiModelRepositoryService aiModelRepo;

    @Autowired
    private AiModelSpecificationRepositoryService specRepo;

    private final List<String> createdModelIds = new ArrayList<>();
    private final List<String> createdSpecIds = new ArrayList<>();
    private final List<String> containerIds = new ArrayList<>();
    private final List<String> imageNames = new ArrayList<>();

    @AfterEach
    void cleanup() {
        // Stop containers and remove images
        for (String containerId : containerIds) {
            try {
                deploymentService.stopContainer(containerId, true);
            } catch (Exception ignored) {
            }
        }
        for (String image : imageNames) {
            try {
                deploymentService.removeImage(image);
            } catch (Exception ignored) {
            }
        }
        // Remove DB records
        for (String id : createdModelIds) {
            try {
                aiModelRepo.deleteAiModel(id);
            } catch (Exception ignored) {
            }
        }
        for (String id : createdSpecIds) {
            try {
                specRepo.deleteAiModelSpecification(id);
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    void supports_withMlflowPlatform_returnsTrue() {
        AiModel model = new AiModel();
        addCharacteristic(model, "platform", "mlflow");
        assertTrue(mlflowDeployer.supports(model));
    }

    @Test
    void supports_withOtherPlatform_returnsFalse() {
        AiModel model = new AiModel();
        addCharacteristic(model, "platform", "kserve");
        assertFalse(mlflowDeployer.supports(model));
    }

    @Test
    void supports_withNoPlatform_returnsFalse() {
        AiModel model = new AiModel();
        assertFalse(mlflowDeployer.supports(model));
    }

    /**
     * Single end-to-end test: deploy → verify state, endpoint, containerId → undeploy → verify stopped.
     * Consolidated to avoid multiple expensive Docker builds and container name conflicts.
     */
    @Test
    @Transactional
    void deployAndUndeploy_fullLifecycle() throws IOException {
        AiModel model = createReservedAiModel();

        // Deploy
        mlflowDeployer.deploy(model);

        // Reload from DB (within transaction for lazy loading)
        AiModel deployed = aiModelRepo.findAiModelById(model.getId());
        assertNotNull(deployed);

        // Verify state
        assertEquals(ServiceStateType.ACTIVE, deployed.getState());

        // Verify endpoint
        String endpoint = getCharacteristicValue(deployed, "endpoint");
        assertNotNull(endpoint, "endpoint characteristic should be set after deploy");
        assertTrue(endpoint.startsWith("http://"), "endpoint should be an HTTP URL");

        // Verify containerId
        String containerId = getCharacteristicValue(deployed, "containerId");
        assertNotNull(containerId, "containerId characteristic should be set after deploy");
        assertFalse(containerId.isEmpty(), "containerId should not be empty");

        // Track for cleanup
        containerIds.add(containerId);
        String imageName = getCharacteristicValue(deployed, "imageName");
        if (imageName != null) imageNames.add(imageName);

        // Undeploy
        mlflowDeployer.undeploy(deployed);

        // Verify container stopped
        boolean canStop = deploymentService.stopContainer(containerId, false);
        assertFalse(canStop, "Container should already be stopped after undeploy");
    }

    @Test
    void deploy_withMissingCharacteristics_throwsIOException() {
        AiModel model = new AiModel();
        model.setId("test-no-chars");
        model.setState(ServiceStateType.RESERVED);
        addCharacteristic(model, "platform", "mlflow");
        // No mlflowModelId or mlflowModelName/mlflowModelVersion

        assertThrows(IOException.class, () -> mlflowDeployer.deploy(model));
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private AiModel createReservedAiModel() throws IOException {
        List<LoggedModel> models = mlflowClientService.searchAllLoggedModels();
        assertFalse(models.isEmpty(), "Need at least one logged model in MLflow");
        LoggedModel lm = models.get(0);

        String experimentName = resolveExperimentName(lm.getInfo().getExperimentId());

        // Create a spec first
        AiModelSpecificationCreate specCreate = new AiModelSpecificationCreate();
        specCreate.setName("IT-Spec-" + experimentName);
        specCreate.setVersion(lm.getInfo().getModelId());
        specCreate.setDescription("Integration test specification");
        AiModelSpecification spec = specRepo.createAiModelSpecification(specCreate);
        createdSpecIds.add(spec.getId());

        // Create an AiModel from the logged model
        AiModelCreate create = mlflowModelService.createReservedAiModelFromLoggedModel(spec, lm, experimentName);
        AiModel created = aiModelRepo.createAiModel(create);
        createdModelIds.add(created.getId());

        return created;
    }

    private String resolveExperimentName(String experimentId) {
        Optional<org.mlflow.api.proto.Service.Experiment> experiment =
                mlflowClientService.getExperiment(experimentId);
        return experiment.map(Experiment::getName).orElse("experiment-" + experimentId);
    }

    private void addCharacteristic(AiModel model, String name, String value) {
        Characteristic c = new Characteristic();
        c.setName(name);
        c.setValue(value);
        c.setValueType("string");
        model.addServiceCharacteristicItem(c);
    }

    private String getCharacteristicValue(AiModel model, String name) {
        if (model.getServiceCharacteristic() == null) return null;
        for (Characteristic c : model.getServiceCharacteristic()) {
            if (name.equals(c.getName())) {
                Object val = c.getValue();
                return val != null ? val.toString() : null;
            }
        }
        return null;
    }
}
