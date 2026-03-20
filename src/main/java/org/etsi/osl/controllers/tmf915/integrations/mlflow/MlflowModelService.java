package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.mlflow.api.proto.ModelRegistry.ModelVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for creating AiModel instances from MLflow models.
 *
 * Follows a two-step deployment lifecycle mirroring the Python
 * {@code mlflow_model_image_builder} pattern:
 * <ol>
 *   <li><b>Build</b> – creates a Docker image from an MLflow run
 *       ({@link MlflowDeploymentService#buildImage})</li>
 *   <li><b>Deploy</b> – starts a container from the image
 *       ({@link MlflowDeploymentService#deployContainer})</li>
 * </ol>
 *
 * Lifecycle states:
 * <ul>
 *   <li>RESERVED – model record created, deployment pending</li>
 *   <li>ACTIVE – container running and serving predictions</li>
 *   <li>TERMINATED – container stopped</li>
 * </ul>
 */
@Service
@ConditionalOnProperty(name = "mlflow.enabled", havingValue = "true")
public class MlflowModelService {

    private static final Logger log = LoggerFactory.getLogger(MlflowModelService.class);

    // Pattern to extract minor version: "modelName v1.2" -> extracts "2"
    private static final Pattern MINOR_VERSION_PATTERN = Pattern.compile("\\.([0-9]+)$");

    private final MlflowClientService mlflowClient;
    private final MlflowDeploymentService deploymentService;
    private final AiModelRepositoryService aiModelRepository;

    public MlflowModelService(MlflowClientService mlflowClient,
                               MlflowDeploymentService deploymentService,
                               AiModelRepositoryService aiModelRepository) {
        this.mlflowClient = mlflowClient;
        this.deploymentService = deploymentService;
        this.aiModelRepository = aiModelRepository;
    }

    // ========================================
    // Lifecycle-aware Deployment Methods
    // ========================================

    /**
     * Creates an AiModel in RESERVED state from an MLflow Logged Model.
     *
     * @param specification The AiModelSpecification this model instantiates
     * @param loggedModel   The logged model from MLflow
     * @param experimentName The experiment name for display
     * @return AiModelCreate in RESERVED state
     */
    public AiModelCreate createReservedAiModelFromLoggedModel(AiModelSpecification specification,
                                                               LoggedModel loggedModel,
                                                               String experimentName) {
        LoggedModel.LoggedModelInfo info = loggedModel.getInfo();
        log.info("Creating RESERVED AiModel for logged model: {} (experiment: {})",
                info.getModelId(), experimentName);

        AiModelCreate model = new AiModelCreate();
        model.setAiModelSpecification(specification);

        String baseName = experimentName + " " + info.getModelId();
        String uniqueName = generateUniqueName(baseName);
        model.setName(uniqueName);

        // Use config.version as description if available
        String configVersion = getLoggedModelParam(loggedModel, "config.version");
        model.setDescription(configVersion != null
                ? configVersion
                : "Pending deployment of " + experimentName + " logged model " + info.getModelId());

        model.setState(ServiceStateType.RESERVED);

        addCharacteristic(model, "platform", "mlflow", "string");
        addCharacteristic(model, "deploymentTarget", "DOCKER", "string");
        addCharacteristic(model, "mlflowModelId", info.getModelId(), "string");
        addCharacteristic(model, "mlflowRunId", info.getSourceRunId(), "string");
        addCharacteristic(model, "mlflowExperimentId", info.getExperimentId(), "string");

        log.info("Created RESERVED AiModel: {}", uniqueName);
        return model;
    }

    /**
     * Builds and deploys from a logged model ID.
     *
     * @param aiModel   persisted AiModel in RESERVED state
     * @param modelId   MLflow logged model ID (e.g. "m-xxx")
     * @param port      preferred host port (null → auto-scan)
     * @param dockerHost Docker host URI (null → default)
     * @return deploy result
     * @throws IOException if any step fails
     */
    public MlflowDeploymentService.DeployResult buildAndDeployFromLoggedModel(AiModel aiModel,
                                                                               String modelId,
                                                                               Integer port,
                                                                               String dockerHost) throws IOException {
        LoggedModel lm = mlflowClient.getLoggedModel(modelId)
                .orElseThrow(() -> new IOException("Logged model not found: " + modelId));

        String artifactUri = lm.getInfo().getArtifactUri();
        if (artifactUri == null || artifactUri.isEmpty()) {
            throw new IOException("Logged model " + modelId + " has no artifact URI");
        }

        String imageName = sanitizeImageName(modelId);
        String containerName = imageName;

        return buildAndDeployFromUri(aiModel, artifactUri, imageName, containerName, port, dockerHost);
    }

    /**
     * Creates an AiModel in RESERVED state before deployment.
     *
     * @param specification The AiModelSpecification this model instantiates
     * @param modelName     MLflow registered model name
     * @param version       Model version (null → latest)
     * @return AiModelCreate in RESERVED state, ready to persist
     * @throws IOException if model information cannot be retrieved from MLflow
     */
    public AiModelCreate createReservedAiModel(AiModelSpecification specification,
                                               String modelName, String version) throws IOException {

        String resolvedVersion = resolveVersion(modelName, version);
        ModelVersion modelVersion = getModelVersion(modelName, resolvedVersion);

        log.info("Creating RESERVED AiModel for MLflow model: {} v{}", modelName, resolvedVersion);

        AiModelCreate model = new AiModelCreate();
        model.setAiModelSpecification(specification);

        String baseName = modelName + " v" + resolvedVersion;
        String uniqueName = generateUniqueName(baseName);
        model.setName(uniqueName);

        String desc = modelVersion.getDescription();
        model.setDescription(desc != null && !desc.isEmpty()
                ? desc
                : "Pending deployment of " + modelVersion.getName() + " v" + modelVersion.getVersion());

        model.setState(ServiceStateType.RESERVED);

        addCharacteristic(model, "platform", "mlflow", "string");
        addCharacteristic(model, "deploymentTarget", "DOCKER", "string");
        addCharacteristic(model, "mlflowModelName", modelName, "string");
        addCharacteristic(model, "mlflowModelVersion", resolvedVersion, "string");
        addCharacteristic(model, "mlflowRunId", modelVersion.getRunId(), "string");

        log.info("Created RESERVED AiModel: {}", uniqueName);
        return model;
    }

    /**
     * Builds a Docker image and deploys a container, then activates the AiModel.
     * <p>
     * Follows the Python pattern: build image (skip if exists) → deploy container.
     *
     * @param aiModel   persisted AiModel in RESERVED state
     * @param runId     MLflow run ID (used for building the image)
     * @param imageName Docker image name
     * @param port      preferred host port (null → auto-scan range)
     * @return deploy result with container ID and endpoint
     * @throws IOException if build or deployment fails
     */
    public MlflowDeploymentService.DeployResult buildAndDeploy(AiModel aiModel,
                                                                String runId, String imageName,
                                                                Integer port) throws IOException {
        return buildAndDeploy(aiModel, runId, imageName, null, port, null);
    }

    /**
     * Builds a Docker image and deploys a container to a specific Docker host.
     *
     * @param aiModel       persisted AiModel in RESERVED state
     * @param runId         MLflow run ID
     * @param imageName     Docker image name
     * @param containerName optional container name
     * @param port          preferred host port (null → auto-scan)
     * @param dockerHost    Docker host URI (null → default from config)
     * @return deploy result
     * @throws IOException if build or deployment fails
     */
    public MlflowDeploymentService.DeployResult buildAndDeploy(AiModel aiModel,
                                                                String runId, String imageName,
                                                                String containerName,
                                                                Integer port,
                                                                String dockerHost) throws IOException {

        log.info("Build-and-deploy: image={}, runId={}, aiModel={}", imageName, runId, aiModel.getId());

        // Step 1 – build (skip if image already exists)
        String targetHost = dockerHost != null ? dockerHost : deploymentService.getDockerHost();
        if (!deploymentService.imageExists(imageName, targetHost)) {
            MlflowDeploymentService.BuildResult buildResult =
                    deploymentService.buildImage(runId, imageName, targetHost);
            if (!buildResult.isSuccess()) {
                throw new IOException("Image build failed: " + buildResult.getMessage());
            }
        } else {
            log.info("Image '{}' already exists – skipping build", imageName);
        }

        // Step 2 – deploy container
        MlflowDeploymentService.DeployResult deployResult =
                deploymentService.deployContainer(imageName, containerName, port, targetHost);

        // Step 3 – update AiModel to ACTIVE
        updateModelAfterDeploy(aiModel, deployResult);

        return deployResult;
    }

    /**
     * Builds a Docker image from an explicit model URI and deploys a container.
     * Used for logged models whose artifacts are stored at a direct S3 URI.
     *
     * @param aiModel       persisted AiModel in RESERVED state
     * @param modelUri      MLflow model URI (e.g. "s3://bucket/path/to/artifacts")
     * @param imageName     Docker image name
     * @param containerName optional container name
     * @param port          preferred host port (null → auto-scan)
     * @param dockerHost    Docker host URI (null → default from config)
     * @return deploy result
     * @throws IOException if build or deployment fails
     */
    public MlflowDeploymentService.DeployResult buildAndDeployFromUri(AiModel aiModel,
                                                                       String modelUri, String imageName,
                                                                       String containerName,
                                                                       Integer port,
                                                                       String dockerHost) throws IOException {

        log.info("Build-and-deploy from URI: image={}, modelUri={}, aiModel={}", imageName, modelUri, aiModel.getId());

        // Step 1 – build (skip if image already exists)
        String targetHost = dockerHost != null ? dockerHost : deploymentService.getDockerHost();
        if (!deploymentService.imageExists(imageName, targetHost)) {
            MlflowDeploymentService.BuildResult buildResult =
                    deploymentService.buildImageFromUri(modelUri, imageName, targetHost);
            if (!buildResult.isSuccess()) {
                throw new IOException("Image build failed: " + buildResult.getMessage());
            }
        } else {
            log.info("Image '{}' already exists – skipping build", imageName);
        }

        // Step 2 – deploy container
        MlflowDeploymentService.DeployResult deployResult =
                deploymentService.deployContainer(imageName, containerName, port, targetHost);

        // Step 3 – update AiModel to ACTIVE
        updateModelAfterDeploy(aiModel, deployResult);

        return deployResult;
    }

    /**
     * Convenience: resolves runId and imageName from MLflow metadata, then builds &amp; deploys.
     *
     * @param aiModel       persisted AiModel in RESERVED state
     * @param modelName     MLflow registered model name
     * @param version       model version (null → latest)
     * @param port          preferred host port (null → auto-scan)
     * @param dockerHost    Docker host URI (null → default)
     * @return deploy result
     * @throws IOException if any step fails
     */
    public MlflowDeploymentService.DeployResult buildAndDeployFromModel(AiModel aiModel,
                                                                         String modelName,
                                                                         String version,
                                                                         Integer port,
                                                                         String dockerHost) throws IOException {
        String resolvedVersion = resolveVersion(modelName, version);
        ModelVersion mv = getModelVersion(modelName, resolvedVersion);
        String runId = mv.getRunId();
        String imageName = sanitizeImageName(modelName) + "-v" + resolvedVersion;
        String containerName = sanitizeImageName(modelName) + "-v" + resolvedVersion;

        return buildAndDeploy(aiModel, runId, imageName, containerName, port, dockerHost);
    }

    /**
     * Stops a container and sets the AiModel to TERMINATED.
     *
     * @param containerId container ID or name
     * @param aiModelId   AiModel ID to update
     * @param remove      also remove the container after stopping
     */
    public void stopAndTerminate(String containerId, String aiModelId, boolean remove) {
        stopAndTerminate(containerId, aiModelId, remove, null);
    }

    /**
     * Stops a container on a specific Docker host and terminates the AiModel.
     */
    public void stopAndTerminate(String containerId, String aiModelId,
                                 boolean remove, String dockerHost) {
        log.info("Stopping container {} and terminating AiModel {}", containerId, aiModelId);
        String targetHost = dockerHost != null ? dockerHost : deploymentService.getDockerHost();
        deploymentService.stopContainer(containerId, remove, targetHost);
        aiModelRepository.updateAiModelState(aiModelId, ServiceStateType.TERMINATED);
        log.info("Container stopped and AiModel {} set to TERMINATED", aiModelId);
    }

    /**
     * Creates an AiModel for a pre-existing external endpoint (no Docker deployment).
     */
    public AiModelCreate createModelInstance(AiModelSpecification specification,
                                             String instanceName, String inferenceUrl) {
        if (inferenceUrl == null || inferenceUrl.isEmpty()) {
            throw new IllegalArgumentException("inferenceUrl is required – AiModel represents a live deployment");
        }

        log.info("Creating AiModel instance from specification: {}", specification.getName());

        AiModelCreate model = new AiModelCreate();
        model.setAiModelSpecification(specification);

        String baseName = instanceName != null ? instanceName : specification.getName() + " Instance";
        String uniqueName = generateUniqueName(baseName);
        model.setName(uniqueName);
        model.setDescription("Instance of " + specification.getName() + " v" + specification.getVersion());
        model.setState(ServiceStateType.ACTIVE);

        addCharacteristic(model, "platform", "mlflow", "string");
        addCharacteristic(model, "endpoint", inferenceUrl, "string");
        addCharacteristic(model, "deployedAt", Instant.now().toString(), "string");
        addCharacteristic(model, "deploymentTarget", "EXTERNAL", "string");

        log.info("Created AiModel instance: {}", uniqueName);
        return model;
    }

    /**
     * Removes a Docker image.
     *
     * @param imageName Docker image to remove
     */
    public void removeImage(String imageName) {
        removeImage(imageName, null);
    }

    /**
     * Removes a Docker image from a specific Docker host.
     */
    public void removeImage(String imageName, String dockerHost) {
        log.info("Removing Docker image: {}", imageName);
        String targetHost = dockerHost != null ? dockerHost : deploymentService.getDockerHost();
        deploymentService.removeImage(imageName, targetHost);
    }

    // ========================================
    // Internal helpers
    // ========================================

    private void updateModelAfterDeploy(AiModel aiModel,
                                        MlflowDeploymentService.DeployResult deploy) {
        addCharacteristicToModel(aiModel, "endpoint", deploy.getInvocationsUrl(), "string");
        addCharacteristicToModel(aiModel, "deployedAt", Instant.now().toString(), "string");

        if (deploy.getDockerHost() != null) {
            addCharacteristicToModel(aiModel, "dockerHost", deploy.getDockerHost(), "string");
        }
        addCharacteristicToModel(aiModel, "containerId", deploy.getContainerId(), "string");
        addCharacteristicToModel(aiModel, "hostPort", String.valueOf(deploy.getHostPort()), "integer");
        addCharacteristicToModel(aiModel, "imageName", deploy.getImageName(), "string");

        aiModel.setState(ServiceStateType.ACTIVE);
        aiModelRepository.updateAiModelState(aiModel.getId(), ServiceStateType.ACTIVE);
        log.info("AiModel {} activated – endpoint {}", aiModel.getId(), deploy.getInvocationsUrl());
    }

    private void addCharacteristicToModel(AiModel model, String name, String value, String valueType) {
        Characteristic characteristic = new Characteristic();
        characteristic.setName(name);
        characteristic.setValue(value);
        characteristic.setValueType(valueType != null ? valueType : "string");
        model.addServiceCharacteristicItem(characteristic);
    }

    private void addCharacteristic(AiModelCreate model, String name, String value, String valueType) {
        Characteristic characteristic = new Characteristic();
        characteristic.setName(name);
        characteristic.setValue(value);
        characteristic.setValueType(valueType != null ? valueType : "string");
        model.addServiceCharacteristicItem(characteristic);
    }

    private String generateUniqueName(String baseName) {
        List<AiModel> existingModels = aiModelRepository.findByNameStartingWith(baseName);
        if (existingModels.isEmpty()) {
            return baseName;
        }

        int maxMinorVersion = 0;
        for (AiModel existing : existingModels) {
            String existingName = existing.getName();
            if (existingName.equals(baseName)) {
                continue;
            }
            Matcher matcher = MINOR_VERSION_PATTERN.matcher(existingName);
            if (matcher.find()) {
                maxMinorVersion = Math.max(maxMinorVersion, Integer.parseInt(matcher.group(1)));
            }
        }
        return baseName + "." + (maxMinorVersion + 1);
    }

    private String resolveVersion(String modelName, String version) throws IOException {
        if (version != null) {
            return version;
        }
        return getLatestModelVersion(modelName).getVersion();
    }

    private ModelVersion getLatestModelVersion(String modelName) throws IOException {
        List<ModelVersion> versions = mlflowClient.getLatestVersions(modelName);
        if (versions.isEmpty()) {
            throw new IOException("No versions found for model: " + modelName);
        }
        return versions.get(0);
    }

    private ModelVersion getModelVersion(String modelName, String version) throws IOException {
        return mlflowClient.getModelVersion(modelName, version)
                .orElseThrow(() -> new IOException(
                        "Version " + version + " not found for model " + modelName));
    }

    /**
     * Sanitizes a model name for use as a Docker image/container name.
     * Matches the Python {@code _sanitize_image_name} logic.
     */
    private static String sanitizeImageName(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9._-]", "-");
    }

    private String getLoggedModelParam(LoggedModel model, String key) {
        if (model.getData() == null || model.getData().getParams() == null) return null;
        for (LoggedModel.Param param : model.getData().getParams()) {
            if (key.equals(param.getKey())) return param.getValue();
        }
        return null;
    }
}
