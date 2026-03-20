package org.etsi.osl.controllers.tmf915.deployment;

import org.etsi.osl.controllers.tmf915.integrations.mlflow.LoggedModel;
import org.etsi.osl.controllers.tmf915.integrations.mlflow.MlflowClientService;
import org.etsi.osl.controllers.tmf915.integrations.mlflow.MlflowDeploymentService;
import org.etsi.osl.controllers.tmf915.integrations.mlflow.MlflowModelService;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the full deployment lifecycle: AiModelLifecycleService + DeploymentScheduler.
 * Tests the TMF 915 API-driven flow: POST/PATCH → schedule → build → deploy → undeploy.
 *
 * <p>Uses the Logged Models API to obtain models for deployment.
 *
 * <p>WARNING: These tests build Docker images and run containers on the remote Docker host.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("integration")
@Tag("integration")
class DeploymentLifecycleIntegrationTest {

    @Autowired
    private AiModelLifecycleService lifecycleService;

    @Autowired
    private DeploymentScheduler scheduler;

    @Autowired
    private AiModelRepositoryService aiModelRepo;

    @Autowired
    private AiModelSpecificationRepositoryService specRepo;

    @Autowired
    private MlflowClientService mlflowClientService;

    @Autowired
    private MlflowModelService mlflowModelService;

    @Autowired
    private MlflowDeploymentService deploymentService;

    @Autowired
    private PlatformTransactionManager txManager;

    private final List<String> createdModelIds = new ArrayList<>();
    private final List<String> createdSpecIds = new ArrayList<>();
    private final List<String> containerIds = new ArrayList<>();
    private final List<String> imageNames = new ArrayList<>();

    @AfterEach
    void cleanup() {
        // Cancel scheduled tasks first to prevent new deploys during cleanup
        for (String id : createdModelIds) {
            try { scheduler.cancelScheduled(id); } catch (Exception ignored) {}
        }
        // Stop containers and remove images
        for (String containerId : containerIds) {
            try { deploymentService.stopContainer(containerId, true); } catch (Exception ignored) {}
        }
        for (String image : imageNames) {
            try { deploymentService.removeImage(image); } catch (Exception ignored) {}
        }
        // Remove DB records
        for (String id : createdModelIds) {
            try { aiModelRepo.deleteAiModel(id); } catch (Exception ignored) {}
        }
        for (String id : createdSpecIds) {
            try { specRepo.deleteAiModelSpecification(id); } catch (Exception ignored) {}
        }
    }

    // ── Create ──────────────────────────────────────────────────────────

    @Test
    void createAiModel_inDesignedState_doesNotDeploy() {
        AiModelCreate create = buildMlflowAiModelCreate();
        create.setState(ServiceStateType.DESIGNED);

        AiModel created = lifecycleService.createAiModel(create);
        createdModelIds.add(created.getId());

        assertNotNull(created.getId());
        assertEquals(ServiceStateType.DESIGNED, created.getState());
    }

    @Test
    void createAiModel_inActiveState_isRejected() {
        AiModelCreate create = buildMlflowAiModelCreate();
        create.setState(ServiceStateType.ACTIVE);

        assertThrows(IllegalArgumentException.class, () -> lifecycleService.createAiModel(create));
    }

    @Test
    void createAiModel_inTerminatedState_isRejected() {
        AiModelCreate create = buildMlflowAiModelCreate();
        create.setState(ServiceStateType.TERMINATED);

        assertThrows(IllegalArgumentException.class, () -> lifecycleService.createAiModel(create));
    }

    @Test
    void createAiModel_inReservedState_setsDefaultDates() {
        AiModelCreate create = buildMlflowAiModelCreate();
        create.setState(ServiceStateType.RESERVED);
        // Don't set start/end dates — they should be defaulted

        AiModel created = lifecycleService.createAiModel(create);
        createdModelIds.add(created.getId());

        assertNotNull(created.getStartDate(), "startDate should be defaulted");
        assertNotNull(created.getEndDate(), "endDate should be defaulted");
        assertTrue(created.getEndDate().isAfter(created.getStartDate()),
                "endDate should be after startDate");

        trackDeployedResources(created.getId());
    }

    @Test
    void createAiModel_inReservedState_triggersDeployment() throws InterruptedException {
        AiModelCreate create = buildMlflowAiModelCreate();
        create.setState(ServiceStateType.RESERVED);

        AiModel created = lifecycleService.createAiModel(create);
        createdModelIds.add(created.getId());

        // The deploy may be async via the scheduler — wait for it
        AiModel deployed = waitForState(created.getId(), ServiceStateType.ACTIVE, 300);
        assertNotNull(deployed, "Model should eventually reach ACTIVE state");
        assertEquals(ServiceStateType.ACTIVE, deployed.getState());

        trackDeployedResources(deployed.getId());
    }

    // ── Update (PATCH) ──────────────────────────────────────────────────

    @Test
    void updateAiModel_toReserved_triggersDeployment() throws InterruptedException {
        // Create in DESIGNED first
        AiModelCreate create = buildMlflowAiModelCreate();
        create.setState(ServiceStateType.DESIGNED);
        AiModel created = lifecycleService.createAiModel(create);
        createdModelIds.add(created.getId());

        // PATCH to RESERVED
        AiModelUpdate update = new AiModelUpdate();
        update.setState(ServiceStateType.RESERVED);
        lifecycleService.updateAiModel(created.getId(), update);

        // Wait for ACTIVE
        AiModel deployed = waitForState(created.getId(), ServiceStateType.ACTIVE, 300);
        assertNotNull(deployed, "Model should eventually reach ACTIVE state after PATCH to RESERVED");

        trackDeployedResources(deployed.getId());
    }

    @Test
    void updateAiModel_toInactive_terminatesModel() throws InterruptedException {
        // Create and deploy first
        AiModelCreate create = buildMlflowAiModelCreate();
        create.setState(ServiceStateType.RESERVED);
        AiModel created = lifecycleService.createAiModel(create);
        createdModelIds.add(created.getId());

        AiModel deployed = waitForState(created.getId(), ServiceStateType.ACTIVE, 300);
        assertNotNull(deployed, "Model should reach ACTIVE state before we can test INACTIVE");

        // Track resources before undeploy since characteristics will still be on the model
        trackDeployedResources(deployed.getId());

        // PATCH to INACTIVE
        AiModelUpdate update = new AiModelUpdate();
        update.setState(ServiceStateType.INACTIVE);
        AiModel terminated = lifecycleService.updateAiModel(deployed.getId(), update);

        assertEquals(ServiceStateType.TERMINATED, terminated.getState(),
                "Model should be TERMINATED after INACTIVE patch");
    }

    // ── Delete ──────────────────────────────────────────────────────────

    @Test
    void deleteAiModel_cancelsScheduledTasks() {
        AiModelCreate create = buildMlflowAiModelCreate();
        create.setState(ServiceStateType.DESIGNED);
        AiModel created = lifecycleService.createAiModel(create);
        String id = created.getId();
        createdModelIds.add(id);

        lifecycleService.deleteAiModel(id);
        createdModelIds.remove(id); // already deleted

        assertNull(aiModelRepo.findAiModelById(id), "Model should be deleted from DB");
    }

    // ── Scheduling with future startDate ─────────────────────────────────

    @Test
    void createAiModel_withFutureStartDate_schedulesForLater() {
        AiModelCreate create = buildMlflowAiModelCreate();
        create.setState(ServiceStateType.RESERVED);
        create.setStartDate(OffsetDateTime.now().plusHours(1));
        create.setEndDate(OffsetDateTime.now().plusHours(2));

        AiModel created = lifecycleService.createAiModel(create);
        createdModelIds.add(created.getId());

        // Should still be RESERVED (not yet deployed)
        AiModel check = aiModelRepo.findAiModelById(created.getId());
        assertEquals(ServiceStateType.RESERVED, check.getState(),
                "Model with future startDate should remain RESERVED");

        // Cancel so it doesn't fire later
        scheduler.cancelScheduled(created.getId());
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private AiModelCreate buildMlflowAiModelCreate() {
        List<LoggedModel> models = mlflowClientService.searchAllLoggedModels();
        assertFalse(models.isEmpty(), "Need at least one logged model in MLflow");
        LoggedModel lm = models.get(0);

        String experimentName = resolveExperimentName(lm.getInfo().getExperimentId());

        // Create a spec
        AiModelSpecificationCreate specCreate = new AiModelSpecificationCreate();
        specCreate.setName("IT-Lifecycle-" + experimentName);
        specCreate.setVersion(lm.getInfo().getModelId());
        specCreate.setDescription("Integration test specification");
        AiModelSpecification spec = specRepo.createAiModelSpecification(specCreate);
        createdSpecIds.add(spec.getId());

        return mlflowModelService.createReservedAiModelFromLoggedModel(spec, lm, experimentName);
    }

    private String resolveExperimentName(String experimentId) {
        Optional<org.mlflow.api.proto.Service.Experiment> experiment =
                mlflowClientService.getExperiment(experimentId);
        return experiment.map(Experiment::getName).orElse("experiment-" + experimentId);
    }

    private AiModel waitForState(String modelId, ServiceStateType expectedState, int maxSeconds)
            throws InterruptedException {
        long deadline = System.currentTimeMillis() + maxSeconds * 1000L;
        while (System.currentTimeMillis() < deadline) {
            AiModel model = aiModelRepo.findAiModelById(modelId);
            if (model != null && model.getState() == expectedState) {
                return model;
            }
            Thread.sleep(2000);
        }
        return aiModelRepo.findAiModelById(modelId);
    }

    private void trackDeployedResources(String modelId) {
        new TransactionTemplate(txManager).executeWithoutResult(status -> {
            AiModel model = aiModelRepo.findAiModelById(modelId);
            if (model == null) return;
            String containerId = getCharacteristicValue(model, "containerId");
            String imageName = getCharacteristicValue(model, "imageName");
            if (containerId != null) containerIds.add(containerId);
            if (imageName != null) imageNames.add(imageName);
        });
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
