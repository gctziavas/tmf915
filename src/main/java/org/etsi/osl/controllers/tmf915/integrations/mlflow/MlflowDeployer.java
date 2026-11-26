package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.deployment.DeploymentException;
import org.etsi.osl.controllers.tmf915.deployment.PlatformDeployer;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * MLflow-specific implementation of {@link PlatformDeployer}.
 * Delegates Docker-based build &amp; deploy to {@link MlflowModelService}
 * and lower-level container operations to {@link MlflowDeploymentService}.
 */
@Service
@ConditionalOnProperty(name = "mlflow.enabled", havingValue = "true")
public class MlflowDeployer implements PlatformDeployer {

    private static final Logger log = LoggerFactory.getLogger(MlflowDeployer.class);

    private final MlflowModelService mlflowModelService;
    private final MlflowDeploymentService deploymentService;

    /**
     * Docker daemons a client may name in the {@code dockerHost} characteristic: the server's
     * own {@code mlflow.docker.host} plus {@code mlflow.docker.allowed-hosts}. Without this
     * limit a client could make the server build images, with artifacts fetched using the
     * server's S3 credentials, on any daemon it names.
     */
    private final Set<String> allowedDockerHosts;

    public MlflowDeployer(MlflowModelService mlflowModelService,
                           MlflowDeploymentService deploymentService,
                           @Value("${mlflow.docker.allowed-hosts:}") String allowedDockerHosts) {
        this.mlflowModelService = mlflowModelService;
        this.deploymentService = deploymentService;
        this.allowedDockerHosts = parseAllowedHosts(deploymentService.getDockerHost(), allowedDockerHosts);
    }

    /** Comma-separated list, plus the default host if one is configured. */
    static Set<String> parseAllowedHosts(String defaultHost, String configured) {
        Set<String> hosts = new LinkedHashSet<>();
        String normalisedDefault = sanitizeDockerHost(defaultHost);
        if (normalisedDefault != null) {
            hosts.add(normalisedDefault);
        }
        if (configured != null) {
            Arrays.stream(configured.split(","))
                    .map(MlflowDeployer::sanitizeDockerHost)
                    .filter(java.util.Objects::nonNull)
                    .forEach(hosts::add);
        }
        return hosts;
    }

    /**
     * Rejects a {@code dockerHost} that is not allowed. An absent or empty value is fine: the
     * deployment then uses {@code mlflow.docker.host}.
     */
    @Override
    public void validateCharacteristics(List<Characteristic> characteristics) {
        for (Characteristic c : characteristics) {
            if ("dockerHost".equals(c.getName())) {
                String requested = c.getValue() != null ? c.getValue().toString() : null;
                if (!isAllowedDockerHost(requested)) {
                    throw new IllegalArgumentException("dockerHost '" + requested + "' is not allowed. "
                            + "Omit it to use the server's default Docker host, or ask the operator to add it "
                            + "to mlflow.docker.allowed-hosts.");
                }
            }
        }
    }

    boolean isAllowedDockerHost(String requested) {
        String normalised = sanitizeDockerHost(requested);
        return normalised == null || allowedDockerHosts.contains(normalised);
    }

    @Override
    public boolean supports(AiModel model) {
        return "mlflow".equals(getCharacteristicValue(model, "platform"));
    }

    /**
     * Builds and starts the container, and returns what to record on the model: the
     * {@link MlflowModelService#DEPLOYMENT_CHARACTERISTICS} that the deployment set on the
     * in-memory {@code model}. Nothing is written to the database here.
     */
    @Override
    public List<Characteristic> deploy(AiModel model) throws IOException {
        try {
            deployUnclassified(model);
            return recordedCharacteristics(model);
        } catch (DeploymentException e) {
            throw e;
        } catch (IOException | RuntimeException e) {
            throw new DeploymentException(publicReason(e), e);
        }
    }

    /**
     * A short, client-safe description of a deployment failure (handoff 5.12). The model's notes
     * show only this; the full message (build output, S3 URLs, paths) is logged by the scheduler.
     */
    private static List<Characteristic> recordedCharacteristics(AiModel model) {
        List<Characteristic> recorded = new java.util.ArrayList<>();
        for (Characteristic c : model.getServiceCharacteristic()) {
            if (c != null && MlflowModelService.DEPLOYMENT_CHARACTERISTICS.contains(c.getName())) {
                Characteristic copy = new Characteristic();
                copy.setName(c.getName());
                copy.setValue(c.getValue());
                copy.setValueType(c.getValueType());
                recorded.add(copy);
            }
        }
        return recorded;
    }

    static String publicReason(Throwable e) {
        String msg = e.getMessage() == null ? "" : e.getMessage().toLowerCase(java.util.Locale.ROOT);
        if (msg.contains("allowed-hosts")) {
            return "Docker host not allowed";
        }
        if (msg.contains("logged model not found")) {
            return "MLflow model not found";
        }
        if (msg.contains("missing mlflowmodelid")) {
            return "The model has no MLflow model reference";
        }
        if (msg.contains("has no artifact uri")) {
            return "Model files not found";
        }
        if (msg.startsWith("image build failed")) {
            if (msg.contains("timed out")) {
                return "Image build timed out";
            }
            if (msg.contains("not found") || msg.contains("nosuchkey") || msg.contains("no such file")) {
                return "Image build failed: model files not found";
            }
            return "Image build failed";
        }
        if (msg.startsWith("container failed to start")) {
            if (msg.contains("port is already allocated") || msg.contains("address already in use")) {
                return "No free host port in the configured range";
            }
            return "Container failed to start";
        }
        return "Deployment failed";
    }

    private void deployUnclassified(AiModel model) throws IOException {
        String dockerHost = sanitizeDockerHost(getCharacteristicValue(model, "dockerHost"));
        // Also checked here, not only on create/PATCH, for rows stored before the allow-list existed.
        if (!isAllowedDockerHost(dockerHost)) {
            throw new IOException("dockerHost '" + dockerHost + "' is not in mlflow.docker.allowed-hosts");
        }

        // Try new Logged Models path first (mlflowModelId characteristic)
        String modelId = getCharacteristicValue(model, "mlflowModelId");
        if (modelId != null) {
            String runId = getCharacteristicValue(model, "mlflowRunId");
            log.info("Deploying AiModel {} via MLflow logged model (modelId={}, runId={}, dockerHost={})",
                    model.getId(), modelId, runId, dockerHost);
            mlflowModelService.buildAndDeployFromLoggedModel(model, modelId, null, dockerHost);
            log.info("AiModel {} deployed successfully", model.getId());
            return;
        }

        // Fall back to classic Model Registry path (mlflowModelName/mlflowModelVersion)
        String name = getCharacteristicValue(model, "mlflowModelName");
        String version = getCharacteristicValue(model, "mlflowModelVersion");
        if (name == null || version == null) {
            throw new IOException("AiModel " + model.getId()
                    + " is missing mlflowModelId or mlflowModelName/mlflowModelVersion characteristics");
        }
        log.info("Deploying AiModel {} via MLflow (model={}, version={}, dockerHost={})", model.getId(), name, version, dockerHost);
        mlflowModelService.buildAndDeployFromModel(model, name, version, null, dockerHost);
        log.info("AiModel {} deployed successfully", model.getId());
    }

    /**
     * Stops the model's container and removes its image, with three safeguards (handoff 5.1, 5.10):
     * <ul>
     *   <li>the container is touched only if it carries this model's
     *       {@value MlflowDeploymentService#LABEL_MODEL_ID} label, or, for containers started
     *       before labels existed, has no label and the name the old scheme gave this model;</li>
     *   <li>the image is kept while another live model still needs it, and is never force-removed;</li>
     *   <li>{@code containerId}, {@code hostPort}, {@code endpoint} etc. are removed from the model.</li>
     * </ul>
     */
    @Override
    public void undeploy(AiModel model) {
        String containerId = getCharacteristicValue(model, "containerId");
        String imageName = getCharacteristicValue(model, "imageName");
        String dockerHost = sanitizeDockerHost(getCharacteristicValue(model, "dockerHost"));
        String targetHost = dockerHost != null ? dockerHost : deploymentService.getDockerHost();

        log.info("Undeploying AiModel {} via MLflow (container={}, image={}, host={})",
                model.getId(), containerId, imageName, targetHost);

        if (containerId != null) {
            MlflowDeploymentService.ContainerOwnership ownership = deploymentService.checkOwnership(
                    containerId, model.getId(), MlflowModelService.legacyContainerName(model), targetHost);
            switch (ownership) {
                case OWNED -> {
                    deploymentService.stopContainer(containerId, true, targetHost);
                    log.info("Container {} stopped and removed", containerId);
                }
                case LEGACY_NAME_MATCH -> {
                    log.warn("Container {} of AiModel {} has no {} label (started before labels were added); "
                                    + "stopping it because its name matches the old naming scheme",
                            containerId, model.getId(), MlflowDeploymentService.LABEL_MODEL_ID);
                    deploymentService.stopContainer(containerId, true, targetHost);
                }
                case NOT_FOUND -> log.info("Container {} of AiModel {} no longer exists", containerId, model.getId());
                case NOT_OWNED -> log.warn("Not stopping container {}: it does not belong to AiModel {}",
                        containerId, model.getId());
            }
        }

        MlflowModelService.clearDeploymentCharacteristics(model);

        if (imageName != null) {
            if (mlflowModelService.isImageNeededByAnotherModel(imageName, model)) {
                log.info("Keeping image {}: another AiModel still uses it", imageName);
            } else {
                deploymentService.removeImage(imageName, targetHost);
            }
        }
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

    /**
     * Sanitizes the dockerHost URI to prevent injection or malformed connections.
     * Keeps common URI parts (alphanumeric, dots, dashes, colons, slashes, @) and removes potentially dangerous shell characters.
     */
    static String sanitizeDockerHost(String dockerHost) {
        if (dockerHost == null || dockerHost.trim().isEmpty()) {
            return null;
        }
        // Remove shell-sensitive characters or whitespace that shouldn't be in a DOCKER_HOST URI.
        // Valid Docker Hosts are generally: tcp://1.2.3.4:2375, unix:///var/run/docker.sock, ssh://user@host
        return dockerHost.trim().replaceAll("[^a-zA-Z0-9://\\.\\-@_]", "");
    }
}
