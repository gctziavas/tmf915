package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.etsi.osl.controllers.tmf915.model.ServiceSpecificationRef;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Container naming (5.2), replacing deployment characteristics (5.13 step 2), image reuse (5.10). */
class MlflowModelServiceUnitTest {

    private final MlflowDeploymentService deploymentService = mock(MlflowDeploymentService.class);
    private final AiModelRepositoryService repository = mock(AiModelRepositoryService.class);
    private final MlflowModelService service =
            new MlflowModelService(mock(MlflowClientService.class), deploymentService, repository);

    private static AiModel model(String id, String name) {
        AiModel m = new AiModel();
        m.setId(id);
        m.setName(name);
        return m;
    }

    private static Characteristic characteristic(String name, String value) {
        Characteristic c = new Characteristic();
        c.setName(name);
        c.setValue(value);
        return c;
    }

    private static List<String> values(AiModel model, String name) {
        return model.getServiceCharacteristic().stream()
                .filter(c -> name.equals(c.getName()))
                .map(c -> String.valueOf(c.getValue()))
                .toList();
    }

    // ── 5.2 ──────────────────────────────────────────────────────────────

    @Test
    void containerNames_differForModelsWithTheSameName() {
        String a = MlflowModelService.generateContainerName(model("11111111-aaaa", "fraud-v-m-8a1e-1"), null);
        String b = MlflowModelService.generateContainerName(model("22222222-bbbb", "fraud-v-m-8a1e-1"), null);
        assertEquals("fraud-v-m-8a1e-1-11111111", a);
        assertEquals("fraud-v-m-8a1e-1-22222222", b);
        assertNotEquals(a, b);
    }

    @Test
    void containerNames_areValidDockerNames() {
        assertEquals("m-weird-name-11111111",
                MlflowModelService.generateContainerName(model("11111111-aaaa", "-Weird Name"), null));
        assertEquals("aimodel-11111111",
                MlflowModelService.generateContainerName(model("11111111-aaaa", null), null));
    }

    @Test
    void legacyContainerName_reproducesTheOldScheme() {
        AiModel m = model("11111111-aaaa", "Fraud Model");
        ServiceSpecificationRef ref = new ServiceSpecificationRef("spec-1");
        ref.setVersion("m-8a1e");
        m.setServiceSpecification(ref);
        assertEquals("fraud-model-v8a1e", MlflowModelService.legacyContainerName(m));

        assertEquals("fraud-model-11111111", MlflowModelService.legacyContainerName(model("11111111-aaaa", "Fraud Model")));
    }

    // ── 5.13 step 2 ──────────────────────────────────────────────────────

    @Test
    void redeploy_replacesDeploymentCharacteristics() throws Exception {
        AiModel m = model("11111111-aaaa", "fraud");
        m.addServiceCharacteristicItem(characteristic("platform", "mlflow"));
        m.addServiceCharacteristicItem(characteristic("containerId", "old-container"));
        m.addServiceCharacteristicItem(characteristic("endpoint", "http://localhost:3000/invocations"));
        m.addServiceCharacteristicItem(characteristic("hostPort", "3000"));

        when(deploymentService.imageExists(anyString(), any())).thenReturn(true);
        when(deploymentService.deployContainer(anyString(), anyString(), any(), any(), eq("11111111-aaaa")))
                .thenReturn(new MlflowDeploymentService.DeployResult("new-container", "http://localhost:3001",
                        "img", true, "ok", null, 3001));
        when(deploymentService.readMlmodelFromContainer(anyString(), any())).thenThrow(new IOException("no MLmodel"));

        service.buildAndDeployFromUri(m, "s3://mlflow/1/m", "img", "fraud-11111111", null, null);

        assertEquals(List.of("new-container"), values(m, "containerId"));
        assertEquals(List.of("http://localhost:3001/invocations"), values(m, "endpoint"));
        assertEquals(List.of("3001"), values(m, "hostPort"));
        assertEquals(List.of("mlflow"), values(m, "platform"));
        assertEquals(1, values(m, "inferencePayloadExample").size());
    }

    @Test
    void clearDeploymentCharacteristics_removesWhatAStoppedModelMustNotAdvertise() {
        AiModel m = model("1", "x");
        for (String name : List.of("platform", "containerId", "hostPort", "endpoint", "deployedAt",
                "inferencePayloadExample", "imageName", "dockerHost")) {
            m.addServiceCharacteristicItem(characteristic(name, "v"));
        }
        MlflowModelService.clearDeploymentCharacteristics(m);
        assertEquals(List.of("platform", "imageName", "dockerHost"),
                m.getServiceCharacteristic().stream().map(Characteristic::getName).toList());
    }

    // ── 5.10 ─────────────────────────────────────────────────────────────

    @Test
    void imageIsNeeded_whileAnotherModelIsActiveOnIt() {
        AiModel self = model("self", "a");
        self.addServiceCharacteristicItem(characteristic("imageName", "m-8a1e"));
        AiModel other = model("other", "b");
        other.addServiceCharacteristicItem(characteristic("imageName", "m-8a1e"));
        when(repository.findByState(ServiceStateType.ACTIVE)).thenReturn(List.of(self, other));
        when(repository.findByState(ServiceStateType.RESERVED)).thenReturn(List.of());

        assertTrue(service.isImageNeededByAnotherModel("m-8a1e", self));
    }

    @Test
    void imageIsNeeded_whileAModelOfTheSameMlflowModelWaitsToDeploy() {
        AiModel self = model("self", "a");
        self.addServiceCharacteristicItem(characteristic("mlflowModelId", "m-8a1e"));
        AiModel waiting = model("waiting", "b");
        waiting.addServiceCharacteristicItem(characteristic("mlflowModelId", "m-8a1e"));
        when(repository.findByState(ServiceStateType.ACTIVE)).thenReturn(List.of());
        when(repository.findByState(ServiceStateType.RESERVED)).thenReturn(List.of(waiting));

        assertTrue(service.isImageNeededByAnotherModel("m-8a1e", self));
    }

    @Test
    void imageIsNotNeeded_whenOnlyThisModelUsesIt() {
        AiModel self = model("self", "a");
        self.addServiceCharacteristicItem(characteristic("imageName", "m-8a1e"));
        when(repository.findByState(ServiceStateType.ACTIVE)).thenReturn(List.of(self));
        when(repository.findByState(ServiceStateType.RESERVED)).thenReturn(List.of());

        assertFalse(service.isImageNeededByAnotherModel("m-8a1e", self));
    }
}
