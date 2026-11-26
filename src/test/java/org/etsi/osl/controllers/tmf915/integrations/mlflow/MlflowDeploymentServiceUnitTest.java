package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;

import static org.etsi.osl.controllers.tmf915.integrations.mlflow.FakeProcessRunner.fail;
import static org.etsi.osl.controllers.tmf915.integrations.mlflow.FakeProcessRunner.ok;
import static org.etsi.osl.controllers.tmf915.integrations.mlflow.FakeProcessRunner.timeout;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** The Docker command layer, driven through a fake runner (handoff 5.2, 5.3, 5.1 step 2, 5.10). */
class MlflowDeploymentServiceUnitTest {

    private static final String PORT_TAKEN =
            "docker: Error response from daemon: driver failed programming external connectivity on endpoint x: "
                    + "Bind for 0.0.0.0:3000 failed: port is already allocated.";

    private FakeProcessRunner runner;
    private MlflowDeploymentService service;

    @BeforeEach
    void setUp() {
        runner = new FakeProcessRunner();
        service = new MlflowDeploymentService(runner);
        ReflectionTestUtils.setField(service, "trackingUri", "http://localhost:5000");
        ReflectionTestUtils.setField(service, "commandTimeoutSeconds", 30);
        ReflectionTestUtils.setField(service, "containerPort", 8080);
        ReflectionTestUtils.setField(service, "hostPortStart", 3000);
        ReflectionTestUtils.setField(service, "hostPortEnd", 7000);
        ReflectionTestUtils.setField(service, "envManager", "local");
    }

    @Test
    void imageExists_callsDockerImageInspect() {
        runner.when("image inspect", ok("[]"));
        assertTrue(service.imageExists("my-image", "tcp://10.0.0.5:2375"));
        assertEquals(List.of("docker", "image", "inspect", "my-image"), runner.calls.get(0).command());
        assertEquals("tcp://10.0.0.5:2375", runner.calls.get(0).env().get("DOCKER_HOST"));
    }

    @Test
    void stopContainer_stopsThenRemoves() {
        assertTrue(service.stopContainer("c123", true, null));
        assertEquals(List.of("docker stop c123", "docker rm c123"),
                runner.calls.stream().map(FakeProcessRunner.Call::line).toList());
    }

    // ── deploy ───────────────────────────────────────────────────────────

    @Test
    void deploy_labelsTheContainerWithModelIdAndAttempt() throws Exception {
        runner.when("docker run", ok("Unable to find…\nabc123def456\n"));

        MlflowDeploymentService.DeployResult result =
                service.deployContainer("img", "model-a-1234abcd", null, null, "model-id-1");

        List<String> run = runner.calls.get(0).command();
        assertTrue(run.contains(MlflowDeploymentService.LABEL_MODEL_ID + "=model-id-1"), run.toString());
        assertTrue(run.stream().anyMatch(a -> a.startsWith(MlflowDeploymentService.LABEL_ATTEMPT + "=")), run.toString());
        assertTrue(run.containsAll(List.of("--name", "model-a-1234abcd", "0.0.0.0:3000:8080")), run.toString());
        assertEquals("abc123def456", result.getContainerId());
        assertEquals(3000, result.getHostPort());
        assertEquals("http://localhost:3000/invocations", result.getInvocationsUrl());
    }

    @Test
    void deploy_nonPortError_makesExactlyOneAttempt() {
        // 5.3: before, any failure was retried on every port from 3000 to 7000.
        runner.when("docker run", fail("Unable to find image 'img:latest' locally\nError: pull access denied"));

        IOException ex = assertThrows(IOException.class,
                () -> service.deployContainer("img", "name-1", null, null, "id-1"));

        assertEquals(1, runner.callsContaining("docker run").size());
        assertTrue(ex.getMessage().contains("pull access denied"), ex.getMessage());
    }

    @Test
    void deploy_portConflict_triesTheNextPort() throws Exception {
        runner.when(cmd -> cmd.contains("0.0.0.0:3000:8080"), fail(PORT_TAKEN))
              .when(cmd -> cmd.contains("0.0.0.0:3001:8080"), fail("Error: listen tcp 0.0.0.0:3001: bind: address already in use"))
              .when("docker run", ok("cid-3002"));

        MlflowDeploymentService.DeployResult result = service.deployContainer("img", "name-1", null, null, "id-1");

        assertEquals(3, runner.callsContaining("docker run").size());
        assertEquals(3002, result.getHostPort());
        assertEquals("cid-3002", result.getContainerId());
    }

    @Test
    void deploy_timeout_doesNotRetry() {
        runner.when("docker run", timeout());
        assertThrows(IOException.class, () -> service.deployContainer("img", "name-1", null, null, "id-1"));
        assertEquals(1, runner.callsContaining("docker run").size());
    }

    @Test
    void deploy_failure_removesOnlyWhatThisAttemptCreated() {
        // 5.2: before, a failed run did "docker rm -f <name>", which could be another model's container.
        // Port 3000 is taken (retry), then the name is taken by another model's container (stop).
        runner.when(cmd -> cmd.contains("0.0.0.0:3000:8080"), fail(PORT_TAKEN))
              .when(cmd -> cmd.contains("0.0.0.0:3001:8080"),
                      fail("Conflict. The container name \"/shared-name\" is already in use by container \"other\"."))
              .when("docker ps", ok("created1\n"));

        assertThrows(IOException.class, () -> service.deployContainer("img", "shared-name", null, null, "id-1"));
        assertEquals(2, runner.callsContaining("docker run").size());

        List<String> attempts = runner.callsContaining("docker run").stream()
                .map(c -> c.command().stream().filter(a -> a.startsWith(MlflowDeploymentService.LABEL_ATTEMPT + "="))
                        .findFirst().orElseThrow())
                .toList();
        List<FakeProcessRunner.Call> cleanups = runner.callsContaining("docker ps -aq");
        assertEquals(attempts.size(), cleanups.size(), "one cleanup per failed attempt");
        for (int i = 0; i < attempts.size(); i++) {
            assertTrue(cleanups.get(i).line().contains("label=" + attempts.get(i)), cleanups.get(i).line());
        }
        for (FakeProcessRunner.Call rm : runner.callsContaining("docker rm")) {
            assertFalse(rm.command().contains("shared-name"), "removed by name: " + rm.line());
            assertEquals(List.of("docker", "rm", "-f", "created1"), rm.command());
        }
    }

    // ── ownership (5.1 step 2) ───────────────────────────────────────────

    @Test
    void ownership_labelDecides() {
        runner.when("inspect", ok("id-1|/name-1"));
        assertEquals(MlflowDeploymentService.ContainerOwnership.OWNED,
                service.checkOwnership("c1", "id-1", "legacy", null));
        assertEquals(MlflowDeploymentService.ContainerOwnership.NOT_OWNED,
                service.checkOwnership("c1", "id-2", "name-1", null), "a label for another model wins over a name match");
    }

    @Test
    void ownership_unlabelledContainer_onlyByLegacyName() {
        runner.when("inspect", ok("<no value>|/fraud-v8a1e"));
        assertEquals(MlflowDeploymentService.ContainerOwnership.LEGACY_NAME_MATCH,
                service.checkOwnership("c1", "id-1", "fraud-v8a1e", null));
        assertEquals(MlflowDeploymentService.ContainerOwnership.NOT_OWNED,
                service.checkOwnership("c1", "id-1", "other-name", null));
        assertEquals(MlflowDeploymentService.ContainerOwnership.NOT_OWNED,
                service.checkOwnership("c1", "id-1", null, null));
    }

    @Test
    void ownership_mysqlContainer_isNotOwned() {
        runner.when("inspect", ok("|/tmf915-mysql-1"));
        assertEquals(MlflowDeploymentService.ContainerOwnership.NOT_OWNED,
                service.checkOwnership("tmf915-mysql-1", "id-1", "probe-v1", null));
    }

    @Test
    void ownership_missingContainer_isNotFound() {
        runner.when("inspect", fail("Error: No such object: c1"));
        assertEquals(MlflowDeploymentService.ContainerOwnership.NOT_FOUND,
                service.checkOwnership("c1", "id-1", null, null));
    }

    // ── images (5.10) and builds ────────────────────────────────────────

    @Test
    void removeImage_isNotForced() {
        service.removeImage("img", null);
        assertEquals(List.of("docker", "rmi", "img"), runner.calls.get(0).command());
    }

    @Test
    void build_fallsBackToVirtualenv_andReportsTheOutputTail() throws Exception {
        runner.when("--env-manager local", fail("x".repeat(10_000) + "\nERROR: No matching distribution found for foo==9"))
              .when("--env-manager virtualenv", fail("virtualenv failed too"));

        MlflowDeploymentService.BuildResult result = service.buildImageFromUri("s3://mlflow/1/m", "img", null);

        assertFalse(result.isSuccess());
        assertEquals(2, runner.callsContaining("build-docker").size());
        assertTrue(result.getMessage().contains("virtualenv failed too"), result.getMessage());
        assertEquals("http://localhost:5000", runner.calls.get(0).env().get("MLFLOW_TRACKING_URI"));
    }

    // ── serving-base-url (handoff 4.6) ──────────────────────────────────

    private String deployedEndpoint(String servingBaseUrl, String defaultHost, String targetHost) throws Exception {
        ReflectionTestUtils.setField(service, "servingBaseUrl", servingBaseUrl);
        ReflectionTestUtils.setField(service, "dockerHost", defaultHost);
        runner.when("docker run", ok("cid"));
        return service.deployContainer("img", "n-1", null, targetHost, "id-1").getInvocationsUrl();
    }

    @Test
    void servingBaseUrl_replacesLocalhostOnTheDefaultHost() throws Exception {
        assertEquals("http://models.example.com:3000/invocations",
                deployedEndpoint("http://models.example.com/", "unix:///var/run/docker.sock", "unix:///var/run/docker.sock"));
    }

    @Test
    void servingBaseUrl_templateWithPort() throws Exception {
        assertEquals("https://gw.example.com/models/3000/invocations",
                deployedEndpoint("https://gw.example.com/models/{port}", "", null));
    }

    @Test
    void servingBaseUrl_doesNotApplyToAnotherDockerHost() throws Exception {
        assertEquals("http://10.0.0.6:3000/invocations",
                deployedEndpoint("http://models.example.com", "unix:///var/run/docker.sock", "tcp://10.0.0.6:2375"));
    }

    @Test
    void withoutServingBaseUrl_theDockerHostAddressIsUsed() throws Exception {
        assertEquals("http://localhost:3000/invocations", deployedEndpoint("", "", null));
        // MlflowModelService always passes the resolved host (the default when none was given).
        assertEquals("http://10.0.0.5:3000/invocations", deployedEndpoint("", "tcp://10.0.0.5:2375", "tcp://10.0.0.5:2375"));
    }

    @Test
    void servingBaseUrl_invalidValuesStopStartup() {
        for (String bad : new String[]{"models.example.com", "ftp://models.example.com",
                "http://models.example.com:8000", "http://models.example.com/models"}) {
            assertThrows(IllegalStateException.class, () -> MlflowDeploymentService.validateServingBaseUrl(bad), bad);
        }
        MlflowDeploymentService.validateServingBaseUrl("");
        MlflowDeploymentService.validateServingBaseUrl("http://models.example.com");
        MlflowDeploymentService.validateServingBaseUrl("https://gw.example.com:8443/models/{port}");
    }
}
