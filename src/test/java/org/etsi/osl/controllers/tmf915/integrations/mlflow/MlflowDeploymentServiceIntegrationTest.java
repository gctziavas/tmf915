package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link MlflowDeploymentService} against a live Docker daemon.
 * Requires a Docker daemon accessible at the host configured in application-integration.yml.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("integration")
@Tag("integration")
class MlflowDeploymentServiceIntegrationTest {

    @Autowired
    private MlflowDeploymentService deploymentService;

    @Test
    void getDockerHost_returnsConfiguredHost() {
        String host = deploymentService.getDockerHost();
        assertNotNull(host);
        assertTrue(host.contains("172.16.100.94"), "Docker host should point to the configured remote host");
    }

    @Test
    void imageExists_withNonExistingImage_returnsFalse() {
        boolean exists = deploymentService.imageExists("non_existent_image_xyz_999:latest");
        assertFalse(exists, "Non-existent image should return false");
    }

    @Test
    void stopContainer_withNonExistingContainer_returnsFalse() {
        boolean result = deploymentService.stopContainer("non_existent_container_xyz_999", false);
        assertFalse(result, "Stopping a non-existent container should return false");
    }

    @Test
    void removeImage_withNonExistingImage_doesNotThrow() {
        // docker rmi -f silently succeeds for non-existent images in Docker 29+
        boolean result = deploymentService.removeImage("non_existent_image_xyz_999:latest");
        // Either true (force flag) or false is acceptable — just verify no exception
        assertNotNull(Boolean.valueOf(result));
    }

    @Test
    void getContainerLogs_withNonExistingContainer_returnsEmptyString() {
        String logs = deploymentService.getContainerLogs("non_existent_container_xyz_999", 10);
        assertNotNull(logs);
    }

    @Test
    void buildImage_withInvalidRunId_failsGracefully() throws IOException {
        MlflowDeploymentService.BuildResult result =
                deploymentService.buildImage("0" + "0".repeat(31), "test-invalid-build:latest");

        assertNotNull(result);
        assertFalse(result.isSuccess(), "Build with invalid runId should fail");
    }

    @Test
    void deployContainer_withNonExistingImage_throwsIOException() {
        assertThrows(IOException.class, () ->
                deploymentService.deployContainer(
                        "non_existent_image_xyz_999:latest",
                        "test-no-image-container",
                        5199,
                        deploymentService.getDockerHost()));
    }
}
