package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for building and deploying MLflow model Docker containers.
 *
 * Mirrors the Python mlflow_model_image_builder pattern:
 * <ol>
 *   <li><b>Build</b> – {@code mlflow models build-docker} creates an image from a run</li>
 *   <li><b>Deploy</b> – {@code docker run} starts a container, scanning a port range</li>
 *   <li><b>Stop</b> – {@code docker stop / docker rm} tears down the container</li>
 * </ol>
 *
 * All Docker interaction runs as subprocesses (no shell) through a {@link ProcessRunner}.
 *
 * <p>Every container is started with two labels: {@value #LABEL_MODEL_ID} (the AiModel it
 * belongs to, checked before it is stopped) and {@value #LABEL_ATTEMPT} (one value per
 * {@code docker run}, used to remove only what a failed attempt created).
 */
@Service
@ConditionalOnProperty(name = "mlflow.enabled", havingValue = "true")
public class MlflowDeploymentService {

    private static final Logger log = LoggerFactory.getLogger(MlflowDeploymentService.class);

    private static final Pattern DOCKER_HOST_PATTERN = Pattern.compile("tcp://([^:]+):");

    /** Container label holding the id of the AiModel the container serves. */
    public static final String LABEL_MODEL_ID = "tmf915.aiModelId";

    /** Container label unique to one {@code docker run} attempt. */
    public static final String LABEL_ATTEMPT = "tmf915.deployAttempt";

    private static final Duration BUILD_TIMEOUT = Duration.ofMinutes(10);

    /** How much command output goes into error messages. */
    private static final int ERROR_OUTPUT_CHARS = 2000;

    private final ProcessRunner processRunner;

    @Value("${mlflow.tracking-uri}")
    private String trackingUri;

    @Value("${mlflow.docker.host:}")
    private String dockerHost;

    @Value("${mlflow.docker.container-port:8080}")
    private int containerPort;

    @Value("${mlflow.docker.host-port-start:3000}")
    private int hostPortStart;

    @Value("${mlflow.docker.host-port-end:7000}")
    private int hostPortEnd;

    @Value("${mlflow.docker.env-manager:local}")
    private String envManager;

    @Value("${mlflow.docker.command-timeout-seconds:30}")
    private int commandTimeoutSeconds;

    @Value("${mlflow.s3-endpoint-url:}")
    private String s3EndpointUrl;

    @Value("${mlflow.s3-access-key:}")
    private String s3AccessKey;

    @Value("${mlflow.s3-secret-key:}")
    private String s3SecretKey;

    public MlflowDeploymentService(ProcessRunner processRunner) {
        this.processRunner = processRunner;
    }

    // ── Build ───────────────────────────────────────────────────────────

    /**
     * Builds a Docker image from an MLflow run.
     * Equivalent to the Python {@code build_image()} function.
     *
     * @param runId     MLflow run ID
     * @param imageName Docker image name (e.g. "iris-classifier:abc12")
     * @return BuildResult with outcome
     * @throws IOException if the build process cannot be started
     */
    public BuildResult buildImage(String runId, String imageName) throws IOException {
        return buildImage(runId, imageName, dockerHost);
    }

    /**
     * Builds a Docker image on a specific Docker host.
     *
     * @param runId      MLflow run ID
     * @param imageName  Docker image name
     * @param targetHost Docker host URI (e.g. "tcp://host:2375"), null for local
     * @return BuildResult with outcome
     * @throws IOException if the build process cannot be started
     */
    public BuildResult buildImage(String runId, String imageName, String targetHost) throws IOException {
        return buildImageFromUri("runs:/" + runId + "/model", imageName, targetHost);
    }

    /**
     * Builds a Docker image from an explicit model URI (e.g. S3 artifact URI for logged models).
     *
     * @param modelUri   MLflow model URI (e.g. "runs:/abc/model" or "s3://bucket/path")
     * @param imageName  Docker image name
     * @param targetHost Docker host URI (e.g. "tcp://host:2375"), null for local
     * @return BuildResult with outcome
     * @throws IOException if the build process cannot be started
     */
    public BuildResult buildImageFromUri(String modelUri, String imageName, String targetHost) throws IOException {
        log.info("Building Docker image '{}' from URI {} (env_manager={})", imageName, modelUri, envManager);

        // Try configured env-manager, fallback to virtualenv if "local" fails
        String[] managersToTry = "local".equals(envManager)
                ? new String[]{envManager, "virtualenv"}
                : new String[]{envManager};

        String lastError = null;
        for (String mgr : managersToTry) {
            List<String> cmd = List.of("mlflow", "models", "build-docker",
                    "--model-uri", modelUri,
                    "--name", imageName,
                    "--env-manager", mgr,
                    "--enable-mlserver");

            Map<String, String> env = new HashMap<>();
            env.put("MLFLOW_TRACKING_URI", trackingUri);
            applyDockerHost(env, targetHost);
            applyS3Env(env);

            log.debug("Executing: {}", String.join(" ", cmd));
            try {
                ProcessRunner.ProcessResult result = processRunner.run(cmd, env, BUILD_TIMEOUT);
                if (result.succeeded()) {
                    log.info("Docker image '{}' built successfully (env_manager={})", imageName, mgr);
                    return new BuildResult(imageName, modelUri, targetHost, true,
                            "Docker image built successfully (env_manager=" + mgr + ")");
                }
                lastError = result.timedOut()
                        ? "Build timed out after " + BUILD_TIMEOUT.toMinutes() + " minutes"
                        : "Build failed (exit " + result.exitCode() + "): " + result.outputTail(ERROR_OUTPUT_CHARS);
                log.warn("Image build with env_manager={} failed: {}", mgr, lastError);
            } catch (IOException e) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new IOException("Build interrupted", e);
                }
                lastError = e.getMessage();
            }
        }

        String msg = "Build failed: " + (lastError != null ? lastError : "unknown error");
        log.error(msg);
        return new BuildResult(imageName, modelUri, targetHost, false, msg);
    }

    /**
     * Checks whether a Docker image already exists.
     */
    public boolean imageExists(String imageName) {
        return imageExists(imageName, dockerHost);
    }

    /**
     * Checks whether a Docker image exists on a specific Docker host.
     */
    public boolean imageExists(String imageName, String targetHost) {
        try {
            return docker(targetHost, "image", "inspect", imageName).succeeded();
        } catch (IOException e) {
            return false;
        }
    }

    // ── Deploy ──────────────────────────────────────────────────────────

    /**
     * Deploys (runs) a Docker container from a previously built image.
     * Scans the configured port range for an available port.
     * Equivalent to the Python {@code deploy_image()} function.
     *
     * @param imageName     Docker image name
     * @param containerName Optional container name (may be null)
     * @return DeployResult with container ID and endpoint
     * @throws IOException if no port is available or docker commands fail
     */
    public DeployResult deployContainer(String imageName, String containerName) throws IOException {
        return deployContainer(imageName, containerName, null, dockerHost, null);
    }

    /**
     * Deploys a container on a specific host port.
     */
    public DeployResult deployContainer(String imageName, String containerName, Integer hostPort) throws IOException {
        return deployContainer(imageName, containerName, hostPort, dockerHost, null);
    }

    /**
     * Deploys a container that is not tied to an AiModel (no {@value #LABEL_MODEL_ID} label).
     */
    public DeployResult deployContainer(String imageName, String containerName,
                                        Integer hostPort, String targetHost) throws IOException {
        return deployContainer(imageName, containerName, hostPort, targetHost, null);
    }

    /**
     * Deploys a container to a specific Docker host, scanning ports if hostPort is null.
     *
     * <p>Only a port conflict moves on to the next port; any other failure (missing image,
     * unreachable daemon, name in use) fails at once. After a failed attempt, only the
     * container that attempt created is removed, found by its {@value #LABEL_ATTEMPT} label;
     * nothing is ever removed by name.
     *
     * @param imageName     Docker image name
     * @param containerName Optional container name
     * @param hostPort      Specific host port, or null to scan range
     * @param targetHost    Docker host URI, or null/empty for local
     * @param aiModelId     AiModel the container serves, stored as {@value #LABEL_MODEL_ID}; may be null
     * @return DeployResult with container ID and endpoint
     * @throws IOException if the container could not be started
     */
    public DeployResult deployContainer(String imageName, String containerName,
                                        Integer hostPort, String targetHost, String aiModelId) throws IOException {

        int firstPort = hostPort != null ? hostPort : hostPortStart;
        int lastPort = hostPort != null ? hostPort : hostPortEnd;

        String lastError = null;
        for (int port = firstPort; port <= lastPort; port++) {
            String attempt = UUID.randomUUID().toString();
            List<String> cmd = new ArrayList<>(List.of("docker", "run", "-d",
                    "-p", "0.0.0.0:" + port + ":" + containerPort,
                    "--label", LABEL_ATTEMPT + "=" + attempt));
            if (aiModelId != null && !aiModelId.isEmpty()) {
                cmd.add("--label");
                cmd.add(LABEL_MODEL_ID + "=" + aiModelId);
            }
            if (containerName != null && !containerName.isEmpty()) {
                cmd.add("--name");
                cmd.add(containerName);
            }
            cmd.add(imageName);

            ProcessRunner.ProcessResult result = processRunner.run(cmd, dockerEnv(targetHost), commandTimeout());
            if (result.succeeded()) {
                String containerId = lastLine(result.stdout());
                String endpoint = "http://" + resolveEndpointHost(targetHost) + ":" + port;
                log.info("Container deployed: image={}, port={}, endpoint={}", imageName, port, endpoint);
                return new DeployResult(containerId, endpoint, imageName, true,
                        "Container deployed successfully on port " + port, targetHost, port);
            }

            lastError = result.timedOut()
                    ? "docker run timed out after " + commandTimeoutSeconds + " s on port " + port
                    : result.outputTail(ERROR_OUTPUT_CHARS);
            removeAttemptContainers(attempt, targetHost);

            if (result.timedOut() || !isPortConflict(result)) {
                break;
            }
            log.debug("Port {} is in use, trying the next one", port);
        }

        String msg = "Container failed to start: " + lastError;
        log.error(msg);
        throw new IOException(msg);
    }

    /** Docker's messages for a host port that is already taken. */
    static boolean isPortConflict(ProcessRunner.ProcessResult result) {
        String output = result.combinedOutput().toLowerCase(Locale.ROOT);
        return output.contains("port is already allocated") || output.contains("address already in use");
    }

    /**
     * Removes the containers a failed {@code docker run} created. Docker creates the container
     * before it binds the port, so a port conflict leaves one behind in "Created" state.
     */
    private void removeAttemptContainers(String attempt, String targetHost) {
        try {
            ProcessRunner.ProcessResult ps = docker(targetHost,
                    "ps", "-aq", "--filter", "label=" + LABEL_ATTEMPT + "=" + attempt);
            for (String id : ps.stdout().split("\\s+")) {
                if (!id.isBlank()) {
                    docker(targetHost, "rm", "-f", id.strip());
                }
            }
        } catch (IOException e) {
            log.warn("Could not clean up after failed docker run (attempt {}): {}", attempt, e.getMessage());
        }
    }

    // ── Ownership ───────────────────────────────────────────────────────

    /** Whether a container may be stopped on behalf of an AiModel. */
    public enum ContainerOwnership {
        /** Labelled with this AiModel's id. */
        OWNED,
        /** No label, but named as the pre-label naming scheme named this model's container. */
        LEGACY_NAME_MATCH,
        /** Belongs to something else; must not be touched. */
        NOT_OWNED,
        /** No such container (already removed). */
        NOT_FOUND
    }

    /**
     * Checks a container against an AiModel before it is stopped or removed.
     *
     * @param legacyName the name the pre-label scheme gave this model's container, or null.
     *                   Accepted only for containers without a {@value #LABEL_MODEL_ID} label,
     *                   which were started before labels existed. Remove in a later release.
     */
    public ContainerOwnership checkOwnership(String containerId, String aiModelId, String legacyName,
                                             String targetHost) {
        try {
            ProcessRunner.ProcessResult result = docker(targetHost, "inspect", "--format",
                    "{{index .Config.Labels \"" + LABEL_MODEL_ID + "\"}}|{{.Name}}", containerId);
            if (!result.succeeded()) {
                return ContainerOwnership.NOT_FOUND;
            }
            String out = lastLine(result.stdout());
            int bar = out.lastIndexOf('|');
            String label = bar >= 0 ? out.substring(0, bar).strip() : "";
            String name = bar >= 0 ? out.substring(bar + 1).strip() : out.strip();
            if (name.startsWith("/")) {
                name = name.substring(1);
            }

            boolean unlabelled = label.isEmpty() || "<no value>".equals(label);
            if (!unlabelled) {
                return label.equals(aiModelId) ? ContainerOwnership.OWNED : ContainerOwnership.NOT_OWNED;
            }
            if (legacyName != null && legacyName.equals(name)) {
                return ContainerOwnership.LEGACY_NAME_MATCH;
            }
            return ContainerOwnership.NOT_OWNED;
        } catch (IOException e) {
            log.warn("Could not inspect container {}: {}", containerId, e.getMessage());
            return ContainerOwnership.NOT_OWNED;
        }
    }

    // ── Stop / Remove ───────────────────────────────────────────────────

    /**
     * Stops a running container.
     *
     * @param containerId Container ID or name
     * @param remove      Also remove the container after stopping
     * @return true if successful
     */
    public boolean stopContainer(String containerId, boolean remove) {
        return stopContainer(containerId, remove, dockerHost);
    }

    /**
     * Stops a running container on a specific Docker host. Callers acting for an AiModel must
     * check {@link #checkOwnership} first.
     */
    public boolean stopContainer(String containerId, boolean remove, String targetHost) {
        try {
            if (!docker(targetHost, "stop", containerId).succeeded()) {
                return false;
            }
            if (remove) {
                docker(targetHost, "rm", containerId);
            }
            log.info("Container stopped: {} (remove={})", containerId, remove);
            return true;
        } catch (IOException e) {
            log.warn("Failed to stop container {}: {}", containerId, e.getMessage());
            return false;
        }
    }

    /**
     * Reads the MLmodel file directly from the running container.
     */
    public String readMlmodelFromContainer(String containerId, String targetHost) throws IOException {
        ProcessRunner.ProcessResult result = docker(targetHost, "exec", containerId, "cat", "/opt/ml/model/MLmodel");
        if (!result.succeeded()) {
            throw new IOException("Failed to read MLmodel from container: " + result.outputTail(ERROR_OUTPUT_CHARS));
        }
        return result.stdout();
    }

    /**
     * Gets logs from a container.
     *
     * @param containerId Container ID or name
     * @param tail        Number of lines from the end, or null for all
     * @return Container logs
     */
    public String getContainerLogs(String containerId, Integer tail) {
        return getContainerLogs(containerId, tail, dockerHost);
    }

    /**
     * Gets logs from a container on a specific Docker host.
     */
    public String getContainerLogs(String containerId, Integer tail, String targetHost) {
        List<String> args = new ArrayList<>(List.of("logs"));
        if (tail != null) {
            args.add("--tail");
            args.add(String.valueOf(tail));
        }
        args.add(containerId);
        try {
            return docker(targetHost, args.toArray(String[]::new)).combinedOutput();
        } catch (IOException e) {
            log.warn("Failed to get logs for container {}: {}", containerId, e.getMessage());
            return "";
        }
    }

    // ── Remove Image ────────────────────────────────────────────────────

    /**
     * Removes a Docker image. Not forced: Docker refuses while a container still uses it.
     *
     * @param imageName image name to remove
     * @param targetHost Docker host URI, or null/empty for local
     * @return true if successful
     */
    public boolean removeImage(String imageName, String targetHost) {
        try {
            ProcessRunner.ProcessResult result = docker(targetHost, "rmi", imageName);
            if (result.succeeded()) {
                log.info("Image removed: {}", imageName);
                return true;
            }
            log.info("Image {} not removed: {}", imageName, result.outputTail(300));
            return false;
        } catch (IOException e) {
            log.warn("Failed to remove image {}: {}", imageName, e.getMessage());
            return false;
        }
    }

    /**
     * Removes a Docker image from the default Docker host.
     */
    public boolean removeImage(String imageName) {
        return removeImage(imageName, dockerHost);
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private ProcessRunner.ProcessResult docker(String targetHost, String... args) throws IOException {
        List<String> cmd = new ArrayList<>(args.length + 1);
        cmd.add("docker");
        cmd.addAll(List.of(args));
        return processRunner.run(cmd, dockerEnv(targetHost), commandTimeout());
    }

    private Duration commandTimeout() {
        return Duration.ofSeconds(commandTimeoutSeconds);
    }

    private Map<String, String> dockerEnv(String targetHost) {
        Map<String, String> env = new HashMap<>();
        applyDockerHost(env, targetHost);
        return env;
    }

    private static String lastLine(String text) {
        String[] lines = text.strip().split("\\R");
        return lines.length == 0 ? "" : lines[lines.length - 1].strip();
    }

    private void applyDockerHost(Map<String, String> env, String targetHost) {
        if (targetHost != null && !targetHost.isEmpty()) {
            env.put("DOCKER_HOST", targetHost);
        }
    }

    private void applyS3Env(Map<String, String> env) {
        if (s3EndpointUrl != null && !s3EndpointUrl.isEmpty()) {
            env.put("MLFLOW_S3_ENDPOINT_URL", s3EndpointUrl);
        }
        if (s3AccessKey != null && !s3AccessKey.isEmpty()) {
            env.put("AWS_ACCESS_KEY_ID", s3AccessKey);
        }
        if (s3SecretKey != null && !s3SecretKey.isEmpty()) {
            env.put("AWS_SECRET_ACCESS_KEY", s3SecretKey);
        }
    }

    private String resolveEndpointHost(String targetHost) {
        if (targetHost != null && !targetHost.isEmpty()) {
            Matcher m = DOCKER_HOST_PATTERN.matcher(targetHost);
            if (m.find()) {
                return m.group(1);
            }
        }
        return "localhost";
    }

    public String getDockerHost() {
        return dockerHost;
    }

    // ── Result DTOs ─────────────────────────────────────────────────────

    /**
     * Result of building a Docker image from an MLflow run.
     */
    public static class BuildResult {
        private final String imageName;
        private final String runId;
        private final String dockerHost;
        private final boolean success;
        private final String message;

        public BuildResult(String imageName, String runId, String dockerHost,
                           boolean success, String message) {
            this.imageName = imageName;
            this.runId = runId;
            this.dockerHost = dockerHost;
            this.success = success;
            this.message = message;
        }

        public String getImageName() { return imageName; }
        public String getRunId() { return runId; }
        public String getDockerHost() { return dockerHost; }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    /**
     * Result of deploying (running) a Docker container.
     */
    public static class DeployResult {
        private final String containerId;
        private final String endpoint;
        private final String imageName;
        private final boolean success;
        private final String message;
        private final String dockerHost;
        private final int hostPort;

        public DeployResult(String containerId, String endpoint, String imageName,
                            boolean success, String message, String dockerHost, int hostPort) {
            this.containerId = containerId;
            this.endpoint = endpoint;
            this.imageName = imageName;
            this.success = success;
            this.message = message;
            this.dockerHost = dockerHost;
            this.hostPort = hostPort;
        }

        public String getContainerId() { return containerId; }
        public String getEndpoint() { return endpoint; }
        /** Endpoint + /invocations — the MLflow model prediction URL. */
        public String getInvocationsUrl() { return endpoint + "/invocations"; }
        public String getImageName() { return imageName; }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getDockerHost() { return dockerHost; }
        public int getHostPort() { return hostPort; }
    }
}
