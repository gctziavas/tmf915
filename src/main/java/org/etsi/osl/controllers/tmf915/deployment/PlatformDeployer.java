package org.etsi.osl.controllers.tmf915.deployment;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.Characteristic;

import java.io.IOException;
import java.util.List;

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

    /**
     * Validates characteristics a client sent on create or update, before anything is stored.
     * Called for every deployer, whatever the requested platform. Throw
     * {@link IllegalArgumentException} to reject the request with {@code 400}.
     * Default: accept everything.
     *
     * @param characteristics the client's characteristics (server-owned ones already removed);
     *                        may be empty, never null
     */
    default void validateCharacteristics(List<Characteristic> characteristics) {
    }
}
