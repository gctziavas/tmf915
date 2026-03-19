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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for creating AiModel instances from MLflow models.
 * 
 * An AiModel represents a live deployment of a model - it links an
 * AiModelSpecification (the blueprint) to an inference endpoint.
 * 
 * Deployments are done via Docker containers on a remote Docker host.
 * 
 * Lifecycle states:
 * - RESERVED: Model created, deployment pending
 * - ACTIVE: Container running and serving predictions
 * - TERMINATED: Container stopped
 */
@Service
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

    /**
     * Deploys an MLflow model to Docker and creates an AiModel.
     * 
     * @deprecated Use {@link #createReservedAiModel} followed by {@link #deployAndActivate}
     *             for lifecycle-aware deployment (RESERVED -> ACTIVE -> TERMINATED).
     */
    @Deprecated
    public AiModelCreate deployAndCreateTmfModel(AiModelSpecification specification,
            String modelName, String version, Integer port) throws IOException {

        if (!deploymentService.isEnabled()) {
            throw new IllegalStateException(
                "Docker deployment is not enabled. Configure mlflow.docker.enabled=true and mlflow.docker.host");
        }

        String resolvedVersion = version;
        if (resolvedVersion == null) {
            ModelVersion latest = mlflowClient.getLatestModelVersion(modelName);
            if (latest == null) {
                throw new IOException("No versions found for model: " + modelName);
            }
            resolvedVersion = latest.getVersion();
        }

        log.info("Deploying MLflow model {} v{} to Docker...", modelName, resolvedVersion);

        MlflowDeploymentService.DeploymentResult deployment;
        if (port != null) {
            deployment = deploymentService.deployModel(modelName, resolvedVersion, port);
        } else {
            deployment = deploymentService.deployModel(modelName, resolvedVersion);
        }

        log.info("Model deployed at: {}", deployment.getInferenceUrl());

        return createModelFromDeployment(specification, modelName, resolvedVersion, deployment);
    }

    /**
     * Deploys an MLflow model to Docker on an auto-assigned port.
     * 
     * @deprecated Use lifecycle-aware methods instead.
     */
    @Deprecated
    public AiModelCreate deployAndCreateTmfModel(AiModelSpecification specification,
            String modelName, String version) throws IOException {
        return deployAndCreateTmfModel(specification, modelName, version, (Integer) null);
    }

    /**
     * Deploys an MLflow model to a CUSTOM Docker host and creates an AiModel.
     * 
     * @deprecated Use {@link #createReservedAiModel} followed by {@link #deployAndActivateToHost}
     *             for lifecycle-aware deployment (RESERVED -> ACTIVE -> TERMINATED).
     */
    @Deprecated
    public AiModelCreate deployAndCreateTmfModelToHost(AiModelSpecification specification,
            String modelName, String version,
            String customDockerHost, int customDockerPort, Integer port) throws IOException {

        String resolvedVersion = version;
        if (resolvedVersion == null) {
            ModelVersion latest = mlflowClient.getLatestModelVersion(modelName);
            if (latest == null) {
                throw new IOException("No versions found for model: " + modelName);
            }
            resolvedVersion = latest.getVersion();
        }

        log.info("Deploying MLflow model {} v{} to custom Docker host {}:{}...",
                modelName, resolvedVersion, customDockerHost, customDockerPort);

        MlflowDeploymentService.DeploymentResult deployment =
            deploymentService.deployModelToHost(modelName, resolvedVersion,
                    customDockerHost, customDockerPort, port);

        log.info("Model deployed at: {}", deployment.getInferenceUrl());

        return createModelFromDeployment(specification, modelName, resolvedVersion, deployment);
    }

    // ========================================
    // Lifecycle-aware Deployment Methods
    // ========================================

    /**
     * Creates an AiModelCreate in RESERVED state for initial persistence.
     * 
     * @param specification The AiModelSpecification this model instantiates
     * @param modelName The MLflow model name
     * @param version The model version
     * @return AiModelCreate in RESERVED state ready to be persisted
     * @throws IOException if model information cannot be retrieved
     */
    public AiModelCreate createReservedAiModel(AiModelSpecification specification,
            String modelName, String version) throws IOException {

        log.info("Creating RESERVED AiModel for MLflow model: {} v{}", modelName, version);

        String resolvedVersion = version;
        if (resolvedVersion == null) {
            ModelVersion latest = mlflowClient.getLatestModelVersion(modelName);
            if (latest == null) {
                throw new IOException("No versions found for model: " + modelName);
            }
            resolvedVersion = latest.getVersion();
        }

        List<ModelVersion> versions = mlflowClient.getModelVersions(modelName);
        final String versionToFind = resolvedVersion;
        ModelVersion modelVersion = versions.stream()
                .filter(v -> v.getVersion().equals(versionToFind))
                .findFirst()
                .orElseThrow(() -> new IOException("Version " + versionToFind + " not found"));

        AiModelCreate model = new AiModelCreate();
        model.setAiModelSpecification(specification);

        String baseName = modelName + " v" + resolvedVersion;
        String uniqueName = generateUniqueName(baseName);
        model.setName(uniqueName);

        if (modelVersion.getDescription() != null && !modelVersion.getDescription().isEmpty()) {
            model.setDescription(modelVersion.getDescription());
        } else {
            model.setDescription("Pending deployment of " + modelVersion.getName() + " v" + modelVersion.getVersion());
        }

        model.setState(ServiceStateType.RESERVED);

        addCharacteristic(model, "platform", "mlflow", "string");
        addCharacteristic(model, "deploymentTarget", "DOCKER", "string");
        addCharacteristic(model, "mlflowModelName", modelName, "string");
        addCharacteristic(model, "mlflowModelVersion", resolvedVersion, "string");

        log.info("Created RESERVED AiModel: {}", uniqueName);
        return model;
    }

    /**
     * Deploys an MLflow model to Docker and updates the AiModel to ACTIVE state.
     * 
     * @param aiModel The persisted AiModel in RESERVED state
     * @param modelName The MLflow model name
     * @param version The model version
     * @param port The preferred host port (null for auto-assign)
     * @return The deployment result with container info
     * @throws IOException if deployment fails
     */
    public MlflowDeploymentService.DeploymentResult deployAndActivate(AiModel aiModel,
            String modelName, String version, Integer port) throws IOException {

        if (!deploymentService.isEnabled()) {
            throw new IllegalStateException(
                "Docker deployment is not enabled. Configure mlflow.docker.enabled=true and mlflow.docker.host");
        }

        log.info("Deploying MLflow model {} v{} for AiModel {}", modelName, version, aiModel.getId());

        MlflowDeploymentService.DeploymentResult deployment;
        if (port != null) {
            deployment = deploymentService.deployModel(modelName, version, port);
        } else {
            deployment = deploymentService.deployModel(modelName, version);
        }

        log.info("Model deployed at: {}, updating AiModel to ACTIVE", deployment.getInferenceUrl());

        updateModelWithDeploymentInfo(aiModel, deployment);
        aiModel.setState(ServiceStateType.ACTIVE);
        aiModelRepository.updateAiModelState(aiModel.getId(), ServiceStateType.ACTIVE);

        return deployment;
    }

    /**
     * Deploys an MLflow model to a CUSTOM Docker host and updates the AiModel to ACTIVE state.
     * 
     * @param aiModel The persisted AiModel in RESERVED state
     * @param modelName The MLflow model name
     * @param version The model version
     * @param customDockerHost The Docker host IP/hostname
     * @param customDockerPort The Docker API port
     * @param port The preferred host port (null for auto-assign)
     * @return The deployment result with container info
     * @throws IOException if deployment fails
     */
    public MlflowDeploymentService.DeploymentResult deployAndActivateToHost(AiModel aiModel,
            String modelName, String version,
            String customDockerHost, int customDockerPort, Integer port) throws IOException {

        log.info("Deploying MLflow model {} v{} to custom host {}:{} for AiModel {}",
                modelName, version, customDockerHost, customDockerPort, aiModel.getId());

        MlflowDeploymentService.DeploymentResult deployment =
            deploymentService.deployModelToHost(modelName, version,
                    customDockerHost, customDockerPort, port);

        log.info("Model deployed at: {}, updating AiModel to ACTIVE", deployment.getInferenceUrl());

        updateModelWithDeploymentInfo(aiModel, deployment);
        aiModel.setState(ServiceStateType.ACTIVE);
        aiModelRepository.updateAiModelState(aiModel.getId(), ServiceStateType.ACTIVE);

        return deployment;
    }

    private void updateModelWithDeploymentInfo(AiModel aiModel,
            MlflowDeploymentService.DeploymentResult deployment) {

        addCharacteristicToModel(aiModel, "endpoint", deployment.getInferenceUrl(), "string");
        addCharacteristicToModel(aiModel, "deployedAt", Instant.now().toString(), "string");

        if (deployment.getDockerHost() != null) {
            addCharacteristicToModel(aiModel, "dockerHost", deployment.getDockerHost(), "string");
        }
        if (deployment.getContainerId() != null) {
            addCharacteristicToModel(aiModel, "containerId", deployment.getContainerId(), "string");
        }
        if (deployment.getContainerName() != null) {
            addCharacteristicToModel(aiModel, "containerName", deployment.getContainerName(), "string");
        }
        if (deployment.getHostPort() > 0) {
            addCharacteristicToModel(aiModel, "hostPort", String.valueOf(deployment.getHostPort()), "integer");
        }
    }

    private void addCharacteristicToModel(AiModel model, String name, String value, String valueType) {
        Characteristic characteristic = new Characteristic();
        characteristic.setName(name);
        characteristic.setValue(value);
        characteristic.setValueType(valueType != null ? valueType : "string");
        model.addServiceCharacteristicItem(characteristic);
    }

    /**
     * Creates an AiModelCreate from an MLflow model deployment.
     * 
     * @param specification The AiModelSpecification this model instantiates
     * @param modelName The MLflow model name
     * @param version The model version
     * @param deployment The deployment result with container info
     * @return AiModelCreate ready to be persisted
     * @throws IOException if model information cannot be retrieved
     */
    public AiModelCreate createModelFromDeployment(AiModelSpecification specification,
            String modelName, String version,
            MlflowDeploymentService.DeploymentResult deployment) throws IOException {

        log.info("Creating AiModel from deployed MLflow model: {} v{}", modelName, version);

        ModelVersion modelVersion;
        String resolvedVersion = version;
        if (resolvedVersion != null) {
            List<ModelVersion> versions = mlflowClient.getModelVersions(modelName);
            final String versionToFind = resolvedVersion;
            modelVersion = versions.stream()
                    .filter(v -> v.getVersion().equals(versionToFind))
                    .findFirst()
                    .orElseThrow(() -> new IOException("Version " + versionToFind + " not found"));
        } else {
            modelVersion = mlflowClient.getLatestModelVersion(modelName);
            if (modelVersion == null) {
                throw new IOException("No versions found for model: " + modelName);
            }
            resolvedVersion = modelVersion.getVersion();
        }

        return buildAiModelCreate(specification, modelName, resolvedVersion, modelVersion, deployment);
    }

    /**
     * Stops a running MLflow model deployment.
     */
    public void stopDeployment(String modelName, String version) throws IOException {
        deploymentService.stopDeployment(modelName, version);
    }

    /**
     * Stops a running MLflow model deployment and updates AiModel to TERMINATED state.
     */
    public void stopDeploymentAndTerminate(String modelName, String version, String aiModelId) throws IOException {
        log.info("Stopping deployment {} v{} and terminating AiModel {}", modelName, version, aiModelId);
        deploymentService.stopDeployment(modelName, version);
        aiModelRepository.updateAiModelState(aiModelId, ServiceStateType.TERMINATED);
        log.info("Deployment stopped and AiModel {} set to TERMINATED", aiModelId);
    }

    /**
     * Stops a running MLflow model deployment on a CUSTOM Docker host.
     */
    public void stopDeploymentOnHost(String modelName, String version,
            String customDockerHost, int customDockerPort) {
        deploymentService.stopDeploymentOnHost(modelName, version, customDockerHost, customDockerPort);
    }

    /**
     * Stops a running MLflow model deployment on a CUSTOM Docker host and updates AiModel to TERMINATED.
     */
    public void stopDeploymentOnHostAndTerminate(String modelName, String version,
            String customDockerHost, int customDockerPort, String aiModelId) {
        log.info("Stopping deployment {} v{} on host {} and terminating AiModel {}",
                modelName, version, customDockerHost, aiModelId);
        deploymentService.stopDeploymentOnHost(modelName, version, customDockerHost, customDockerPort);
        aiModelRepository.updateAiModelState(aiModelId, ServiceStateType.TERMINATED);
        log.info("Deployment stopped on host {} and AiModel {} set to TERMINATED", customDockerHost, aiModelId);
    }

    /**
     * Checks if a model is currently deployed and serving.
     */
    public boolean isDeployed(String modelName, String version) {
        return deploymentService.isDeployed(modelName, version);
    }

    /**
     * Checks if a model is currently deployed on a CUSTOM Docker host.
     */
    public boolean isDeployedOnHost(String modelName, String version,
            String customDockerHost, int customDockerPort) {
        return deploymentService.isDeployedOnHost(modelName, version, customDockerHost, customDockerPort);
    }

    /**
     * Gets the inference URL for a deployed model.
     */
    public String getInferenceUrl(String modelName, String version) {
        return deploymentService.getInferenceUrl(modelName, version);
    }

    /**
     * Gets the inference URL for a deployed model on a CUSTOM Docker host.
     */
    public String getInferenceUrlOnHost(String modelName, String version, String customDockerHost) {
        return deploymentService.getInferenceUrlOnHost(modelName, version, customDockerHost);
    }

    /**
     * Creates an AiModelCreate for a model being instantiated from a specification.
     */
    public AiModelCreate createModelInstance(AiModelSpecification specification,
            String instanceName, String inferenceUrl) {

        if (inferenceUrl == null || inferenceUrl.isEmpty()) {
            throw new IllegalArgumentException("inferenceUrl is required - AiModel represents a live deployment");
        }

        log.info("Creating AiModel instance from specification: {}", specification.getName());

        AiModelCreate model = new AiModelCreate();
        model.setAiModelSpecification(specification);

        String baseName = instanceName != null ? instanceName : specification.getName() + " Instance";
        String uniqueName = generateUniqueName(baseName);
        model.setName(uniqueName);
        model.setDescription("Instance of " + specification.getName() + " v" + specification.getVersion());

        model.setState(ServiceStateType.ACTIVE);

        addDeploymentConfiguration(model, inferenceUrl);

        log.info("Created AiModel instance: {}", uniqueName);
        return model;
    }

    private AiModelCreate buildAiModelCreate(AiModelSpecification specification, String modelName,
            String version, ModelVersion modelVersion,
            MlflowDeploymentService.DeploymentResult deployment) {

        AiModelCreate model = new AiModelCreate();
        model.setAiModelSpecification(specification);

        String baseName = modelName + " v" + version;
        String uniqueName = generateUniqueName(baseName);
        model.setName(uniqueName);

        if (modelVersion.getDescription() != null && !modelVersion.getDescription().isEmpty()) {
            model.setDescription(modelVersion.getDescription());
        } else {
            model.setDescription("Deployed instance of " + modelVersion.getName() + " v" + modelVersion.getVersion());
        }

        model.setState(ServiceStateType.ACTIVE);
        addDeploymentConfiguration(model, deployment);

        log.info("Created AiModel '{}' at {}", uniqueName, deployment.getInferenceUrl());
        return model;
    }

    private void addDeploymentConfiguration(AiModelCreate model,
            MlflowDeploymentService.DeploymentResult deployment) {

        addCharacteristic(model, "platform", "mlflow", "string");
        addCharacteristic(model, "endpoint", deployment.getInferenceUrl(), "string");
        addCharacteristic(model, "deployedAt", Instant.now().toString(), "string");
        addCharacteristic(model, "deploymentTarget", "DOCKER", "string");

        if (deployment.getDockerHost() != null) {
            addCharacteristic(model, "dockerHost", deployment.getDockerHost(), "string");
        }
        if (deployment.getContainerId() != null) {
            addCharacteristic(model, "containerId", deployment.getContainerId(), "string");
        }
        if (deployment.getContainerName() != null) {
            addCharacteristic(model, "containerName", deployment.getContainerName(), "string");
        }
        if (deployment.getHostPort() > 0) {
            addCharacteristic(model, "hostPort", String.valueOf(deployment.getHostPort()), "integer");
        }
    }

    private void addDeploymentConfiguration(AiModelCreate model, String inferenceUrl) {
        addCharacteristic(model, "platform", "mlflow", "string");
        addCharacteristic(model, "endpoint", inferenceUrl, "string");
        addCharacteristic(model, "deployedAt", Instant.now().toString(), "string");
        addCharacteristic(model, "deploymentTarget", "EXTERNAL", "string");
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
                maxMinorVersion = Math.max(maxMinorVersion, 0);
                continue;
            }

            Matcher matcher = MINOR_VERSION_PATTERN.matcher(existingName);
            if (matcher.find()) {
                int minorVersion = Integer.parseInt(matcher.group(1));
                maxMinorVersion = Math.max(maxMinorVersion, minorVersion);
            }
        }

        int nextMinorVersion = maxMinorVersion + 1;
        return baseName + "." + nextMinorVersion;
    }

    private void addCharacteristic(AiModelCreate model, String name, String value, String valueType) {
        Characteristic characteristic = new Characteristic();
        characteristic.setName(name);
        characteristic.setValue(value);
        characteristic.setValueType(valueType != null ? valueType : "string");
        model.addServiceCharacteristicItem(characteristic);
    }
}
