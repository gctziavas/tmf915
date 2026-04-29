package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.deployment.PlatformDeployer;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

    public MlflowDeployer(MlflowModelService mlflowModelService,
                           MlflowDeploymentService deploymentService) {
        this.mlflowModelService = mlflowModelService;
        this.deploymentService = deploymentService;
    }

    @Override
    public boolean supports(AiModel model) {
        return "mlflow".equals(getCharacteristicValue(model, "platform"));
    }

    @Override
    public void deploy(AiModel model) throws IOException {
        String dockerHost = sanitizeDockerHost(getCharacteristicValue(model, "dockerHost"));

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

    @Override
    public void undeploy(AiModel model) {
        String containerId = getCharacteristicValue(model, "containerId");
        String imageName = getCharacteristicValue(model, "imageName");
        String dockerHost = sanitizeDockerHost(getCharacteristicValue(model, "dockerHost"));
        String targetHost = dockerHost != null ? dockerHost : deploymentService.getDockerHost();

        log.info("Undeploying AiModel {} via MLflow (container={}, image={}, host={})",
                model.getId(), containerId, imageName, targetHost);

        if (containerId != null) {
            deploymentService.stopContainer(containerId, true, targetHost);
            log.info("Container {} stopped and removed", containerId);
        }
        if (imageName != null) {
            deploymentService.removeImage(imageName, targetHost);
            log.info("Image {} removed", imageName);
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
    private String sanitizeDockerHost(String dockerHost) {
        if (dockerHost == null || dockerHost.trim().isEmpty()) {
            return null;
        }
        // Remove shell-sensitive characters or whitespace that shouldn't be in a DOCKER_HOST URI.
        // Valid Docker Hosts are generally: tcp://1.2.3.4:2375, unix:///var/run/docker.sock, ssh://user@host
        return dockerHost.trim().replaceAll("[^a-zA-Z0-9://\\.\\-@_]", "");
    }
}
