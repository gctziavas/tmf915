package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
 * All Docker interaction is done via shell subprocess calls,
 * exactly like the Python reference implementation.
 */
@Service
@ConditionalOnProperty(name = "mlflow.enabled", havingValue = "true")
public class MlflowDeploymentService {

    private static final Logger log = LoggerFactory.getLogger(MlflowDeploymentService.class);

    private static final Pattern DOCKER_HOST_PATTERN = Pattern.compile("tcp://([^:]+):");

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

        Exception lastError = null;
        for (String mgr : managersToTry) {
            List<String> cmd = new ArrayList<>();
            cmd.add("mlflow");
            cmd.add("models");
            cmd.add("build-docker");
            cmd.add("--model-uri");
            cmd.add(modelUri);
            cmd.add("--name");
            cmd.add(imageName);
            cmd.add("--env-manager");
            cmd.add(mgr);

            try {
                ProcessBuilder pb = new ProcessBuilder(cmd);
                Map<String, String> env = pb.environment();
                env.put("MLFLOW_TRACKING_URI", trackingUri);
                applyDockerHost(env, targetHost);
                applyS3Env(env);

                log.debug("Executing: {}", String.join(" ", cmd));
                Process process = pb.start();
                boolean finished = process.waitFor(10, TimeUnit.MINUTES);
                if (!finished) {
                    process.destroyForcibly();
                    lastError = new IOException("Build timed out after 10 minutes");
                    continue;
                }
                if (process.exitValue() == 0) {
                    log.info("Docker image '{}' built successfully (env_manager={})", imageName, mgr);
                    return new BuildResult(imageName, modelUri, targetHost, true,
                            "Docker image built successfully (env_manager=" + mgr + ")");
                }
                String stderr = new String(process.getErrorStream().readAllBytes());
                lastError = new IOException("Build failed (exit " + process.exitValue() + "): " + stderr);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Build interrupted", e);
            } catch (IOException e) {
                lastError = e;
            }
        }

        String msg = "Build failed: " + (lastError != null ? lastError.getMessage() : "unknown error");
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
            ProcessBuilder pb = new ProcessBuilder("docker", "image", "inspect", imageName);
            applyDockerHost(pb.environment(), targetHost);
            Process process = pb.start();
            boolean finished = process.waitFor(commandTimeoutSeconds, TimeUnit.SECONDS);
            return finished && process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
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
        return deployContainer(imageName, containerName, null, dockerHost);
    }

    /**
     * Deploys a container on a specific host port.
     */
    public DeployResult deployContainer(String imageName, String containerName, Integer hostPort) throws IOException {
        return deployContainer(imageName, containerName, hostPort, dockerHost);
    }

    /**
     * Deploys a container to a specific Docker host, scanning ports if hostPort is null.
     *
     * @param imageName     Docker image name
     * @param containerName Optional container name
     * @param hostPort      Specific host port, or null to scan range
     * @param targetHost    Docker host URI, or null/empty for local
     * @return DeployResult with container ID and endpoint
     * @throws IOException if deployment fails on all ports
     */
    public DeployResult deployContainer(String imageName, String containerName,
                                        Integer hostPort, String targetHost) throws IOException {

        int[] portsToTry;
        if (hostPort != null) {
            portsToTry = new int[]{hostPort};
        } else {
            portsToTry = new int[hostPortEnd - hostPortStart + 1];
            for (int i = 0; i < portsToTry.length; i++) {
                portsToTry[i] = hostPortStart + i;
            }
        }

        String lastError = null;
        for (int port : portsToTry) {
            List<String> cmd = new ArrayList<>();
            cmd.add("docker");
            cmd.add("run");
            cmd.add("-d");
            cmd.add("-p");
            cmd.add("0.0.0.0:" + port + ":" + containerPort);
            if (containerName != null && !containerName.isEmpty()) {
                cmd.add("--name");
                cmd.add(containerName);
            }
            cmd.add(imageName);

            try {
                ProcessBuilder pb = new ProcessBuilder(cmd);
                applyDockerHost(pb.environment(), targetHost);
                pb.redirectErrorStream(false);

                Process process = pb.start();
                boolean finished = process.waitFor(commandTimeoutSeconds, TimeUnit.SECONDS);
                if (!finished) {
                    process.destroyForcibly();
                    lastError = "Docker command timed out on port " + port;
                    if (containerName != null && !containerName.isEmpty()) {
                        try {
                            ProcessBuilder rmPb = new ProcessBuilder("docker", "rm", "-f", containerName);
                            applyDockerHost(rmPb.environment(), targetHost);
                            rmPb.start().waitFor(10, TimeUnit.SECONDS);
                        } catch (Exception ignored) {}
                    }
                    continue;
                }
                if (process.exitValue() != 0) {
                    lastError = new String(process.getErrorStream().readAllBytes()).trim();
                    if (containerName != null && !containerName.isEmpty()) {
                        try {
                            ProcessBuilder rmPb = new ProcessBuilder("docker", "rm", "-f", containerName);
                            applyDockerHost(rmPb.environment(), targetHost);
                            rmPb.start().waitFor(10, TimeUnit.SECONDS);
                        } catch (Exception ignored) {}
                    }
                    continue;
                }

                String containerId = new String(process.getInputStream().readAllBytes()).trim();
                String endpointHost = resolveEndpointHost(targetHost);
                String endpoint = "http://" + endpointHost + ":" + port;

                log.info("Container deployed: image={}, port={}, endpoint={}", imageName, port, endpoint);
                return new DeployResult(containerId, endpoint, imageName, true,
                        "Container deployed successfully on port " + port, targetHost, port);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Deploy interrupted", e);
            }
        }

        String msg = "Deployment failed on all attempted ports: " + lastError;
        log.error(msg);
        throw new IOException(msg);
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
     * Stops a running container on a specific Docker host.
     */
    public boolean stopContainer(String containerId, boolean remove, String targetHost) {
        try {
            ProcessBuilder pb = new ProcessBuilder("docker", "stop", containerId);
            applyDockerHost(pb.environment(), targetHost);
            Process process = pb.start();
            boolean finished = process.waitFor(commandTimeoutSeconds, TimeUnit.SECONDS);
            if (!finished || process.exitValue() != 0) {
                return false;
            }
            if (remove) {
                ProcessBuilder rmPb = new ProcessBuilder("docker", "rm", containerId);
                applyDockerHost(rmPb.environment(), targetHost);
                rmPb.start().waitFor(commandTimeoutSeconds, TimeUnit.SECONDS);
            }
            log.info("Container stopped: {} (remove={})", containerId, remove);
            return true;
        } catch (IOException | InterruptedException e) {
            log.warn("Failed to stop container {}: {}", containerId, e.getMessage());
            return false;
        }
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
        try {
            List<String> cmd = new ArrayList<>();
            cmd.add("docker");
            cmd.add("logs");
            if (tail != null) {
                cmd.add("--tail");
                cmd.add(String.valueOf(tail));
            }
            cmd.add(containerId);

            ProcessBuilder pb = new ProcessBuilder(cmd);
            applyDockerHost(pb.environment(), targetHost);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor(commandTimeoutSeconds, TimeUnit.SECONDS);
            return new String(process.getInputStream().readAllBytes());
        } catch (IOException | InterruptedException e) {
            log.warn("Failed to get logs for container {}: {}", containerId, e.getMessage());
            return "";
        }
    }

    // ── Remove Image ────────────────────────────────────────────────────

    /**
     * Removes a Docker image.
     *
     * @param imageName image name to remove
     * @param targetHost Docker host URI, or null/empty for local
     * @return true if successful
     */
    public boolean removeImage(String imageName, String targetHost) {
        try {
            ProcessBuilder pb = new ProcessBuilder("docker", "rmi", "-f", imageName);
            applyDockerHost(pb.environment(), targetHost);
            Process process = pb.start();
            boolean finished = process.waitFor(commandTimeoutSeconds, TimeUnit.SECONDS);
            if (finished && process.exitValue() == 0) {
                log.info("Image removed: {}", imageName);
                return true;
            }
            return false;
        } catch (IOException | InterruptedException e) {
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
