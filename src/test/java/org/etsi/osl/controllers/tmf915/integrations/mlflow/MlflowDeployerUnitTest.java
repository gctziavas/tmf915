package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** The dockerHost allow-list (handoff 5.1, step 4). */
class MlflowDeployerUnitTest {

    private final MlflowModelService modelService = mock(MlflowModelService.class);
    private final MlflowDeploymentService deploymentService = mock(MlflowDeploymentService.class);

    private MlflowDeployer deployer(String defaultHost, String allowedHosts) {
        when(deploymentService.getDockerHost()).thenReturn(defaultHost);
        return new MlflowDeployer(modelService, deploymentService, allowedHosts);
    }

    private static Characteristic characteristic(String name, String value) {
        Characteristic c = new Characteristic();
        c.setName(name);
        c.setValue(value);
        return c;
    }

    @Test
    void allowList_isTheDefaultHostPlusConfiguredHosts() {
        assertEquals(Set.of("unix:///var/run/docker.sock", "tcp://10.0.0.5:2375", "tcp://10.0.0.6:2375"),
                MlflowDeployer.parseAllowedHosts("unix:///var/run/docker.sock",
                        " tcp://10.0.0.5:2375 , tcp://10.0.0.6:2375,,"));
        assertEquals(Set.of(), MlflowDeployer.parseAllowedHosts("", ""));
    }

    @Test
    void absentOrEmptyDockerHost_isAlwaysAllowed() {
        MlflowDeployer deployer = deployer("", "");
        assertTrue(deployer.isAllowedDockerHost(null));
        assertTrue(deployer.isAllowedDockerHost(""));
        assertTrue(deployer.isAllowedDockerHost("   "));
    }

    @Test
    void defaultHost_isAllowedWithoutConfiguration() {
        MlflowDeployer deployer = deployer("unix:///var/run/docker.sock", "");
        assertTrue(deployer.isAllowedDockerHost("unix:///var/run/docker.sock"));
        assertFalse(deployer.isAllowedDockerHost("tcp://attacker.example:2375"));
    }

    @Test
    void configuredHost_isAllowed_othersAreNot() {
        MlflowDeployer deployer = deployer("", "tcp://10.0.0.5:2375");
        assertTrue(deployer.isAllowedDockerHost("tcp://10.0.0.5:2375"));
        assertTrue(deployer.isAllowedDockerHost(" tcp://10.0.0.5:2375 "));
        assertFalse(deployer.isAllowedDockerHost("tcp://10.0.0.6:2375"));
        assertFalse(deployer.isAllowedDockerHost("ssh://user@10.0.0.5"));
    }

    @Test
    void validateCharacteristics_rejectsDisallowedDockerHost_only() {
        MlflowDeployer deployer = deployer("", "tcp://10.0.0.5:2375");
        assertDoesNotThrow(() -> deployer.validateCharacteristics(List.of(
                characteristic("platform", "mlflow"), characteristic("dockerHost", "tcp://10.0.0.5:2375"))));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> deployer.validateCharacteristics(List.of(characteristic("dockerHost", "tcp://evil:2375"))));
        assertTrue(ex.getMessage().contains("mlflow.docker.allowed-hosts"), ex.getMessage());
    }

    @Test
    void deploy_refusesStoredDisallowedDockerHost() throws Exception {
        // Rows stored before the allow-list existed are checked again at deploy time.
        MlflowDeployer deployer = deployer("", "");
        AiModel model = new AiModel();
        model.setId("m1");
        model.addServiceCharacteristicItem(characteristic("platform", "mlflow"));
        model.addServiceCharacteristicItem(characteristic("mlflowModelId", "m-123"));
        model.addServiceCharacteristicItem(characteristic("dockerHost", "tcp://evil:2375"));

        assertThrows(IOException.class, () -> deployer.deploy(model));
        verify(modelService, never()).buildAndDeployFromLoggedModel(any(), any(), any(), any());
    }

    // ── undeploy safeguards (5.1 step 2, 5.10, 5.13 step 2) ──────────────

    private AiModel deployedModel() {
        AiModel model = new AiModel();
        model.setId("model-1");
        model.setName("fraud");
        model.addServiceCharacteristicItem(characteristic("platform", "mlflow"));
        model.addServiceCharacteristicItem(characteristic("containerId", "c-1"));
        model.addServiceCharacteristicItem(characteristic("imageName", "m-8a1e"));
        model.addServiceCharacteristicItem(characteristic("hostPort", "3000"));
        model.addServiceCharacteristicItem(characteristic("endpoint", "http://localhost:3000/invocations"));
        return model;
    }

    private void ownership(MlflowDeploymentService.ContainerOwnership result) {
        when(deploymentService.checkOwnership(eq("c-1"), eq("model-1"), any(), any())).thenReturn(result);
    }

    private static List<String> names(AiModel model) {
        return model.getServiceCharacteristic().stream().map(Characteristic::getName).toList();
    }

    @Test
    void undeploy_doesNotStopAContainerOfSomethingElse() {
        // A row written before input filtering could hold containerId=<mysql container>.
        MlflowDeployer deployer = deployer("", "");
        ownership(MlflowDeploymentService.ContainerOwnership.NOT_OWNED);
        AiModel model = deployedModel();

        deployer.undeploy(model);

        verify(deploymentService, never()).stopContainer(anyString(), anyBoolean(), any());
        assertFalse(names(model).contains("containerId"));
        assertFalse(names(model).contains("endpoint"));
    }

    @Test
    void undeploy_stopsOwnedAndLegacyContainers() {
        for (MlflowDeploymentService.ContainerOwnership o : List.of(
                MlflowDeploymentService.ContainerOwnership.OWNED,
                MlflowDeploymentService.ContainerOwnership.LEGACY_NAME_MATCH)) {
            org.mockito.Mockito.clearInvocations(deploymentService);
            MlflowDeployer deployer = deployer("", "");
            ownership(o);

            deployer.undeploy(deployedModel());

            verify(deploymentService).stopContainer("c-1", true, "");
        }
    }

    @Test
    void undeploy_keepsAnImageAnotherModelNeeds() {
        MlflowDeployer deployer = deployer("", "");
        ownership(MlflowDeploymentService.ContainerOwnership.OWNED);
        when(modelService.isImageNeededByAnotherModel(eq("m-8a1e"), any())).thenReturn(true);

        deployer.undeploy(deployedModel());

        verify(deploymentService, never()).removeImage(anyString(), any());
    }

    @Test
    void undeploy_removesAnImageNobodyElseNeeds() {
        MlflowDeployer deployer = deployer("", "");
        ownership(MlflowDeploymentService.ContainerOwnership.OWNED);
        when(modelService.isImageNeededByAnotherModel(eq("m-8a1e"), any())).thenReturn(false);

        deployer.undeploy(deployedModel());

        verify(deploymentService).removeImage("m-8a1e", "");
    }
}
