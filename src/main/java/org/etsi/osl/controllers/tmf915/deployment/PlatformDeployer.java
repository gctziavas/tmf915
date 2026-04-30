package org.etsi.osl.controllers.tmf915.deployment;

import org.etsi.osl.controllers.tmf915.model.AiModel;

import java.io.IOException;

/**
 * Abstraction for platform-specific deployment operations.
 *
 * <p>Each integration (MLflow, ONNX, custom, …) implements this interface
 * so the {@link DeploymentScheduler} and {@link AiModelLifecycleService}
 * remain platform-agnostic.
 */
public interface PlatformDeployer {

    /**
     * Returns {@code true} if this deployer can handle the given model
     * (typically by inspecting a {@code platform} characteristic).
     */
    boolean supports(AiModel model);

    /**
     * Builds/pushes images and starts a container (or equivalent) for the model.
     * Implementations are expected to update the model's state to ACTIVE and
     * persist relevant characteristics (endpoint, containerId, …).
     */
    void deploy(AiModel model) throws IOException;

    /**
     * Stops and cleans up the running deployment for the model
     * (stop container, remove image, etc.).
     * Does <em>not</em> change the model's persisted state — the caller handles that.
     */
    void undeploy(AiModel model);
}
