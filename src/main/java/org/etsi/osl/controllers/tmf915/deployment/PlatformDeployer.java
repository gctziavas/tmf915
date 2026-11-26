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
     * Starts serving the model and returns the characteristics to record on it: at least an
     * {@code endpoint}, plus whatever {@link #undeploy} needs later (container ids and so on).
     *
     * <p>Runs on a scheduler thread <b>outside any database transaction</b>, and may take
     * minutes. It must not write the model: the scheduler records the returned characteristics
     * and sets the state to {@code active} in a short transaction afterwards. If the model was
     * stopped or deleted while this ran, the scheduler calls {@link #undeploy} on a copy carrying
     * the returned characteristics instead.
     *
     * @return characteristics to set on the model, replacing existing ones with the same name
     * @throws IOException on failure, preferably a {@link DeploymentException} with a
     *                     client-safe reason; the scheduler then sets the model to {@code designed}
     */
    List<Characteristic> deploy(AiModel model) throws IOException;

    /**
     * Stops and cleans up the running deployment for the model (stop container, remove image,
     * etc.), and removes the characteristics that described it from
     * {@code model.getServiceCharacteristic()}. Does <em>not</em> write the model: the caller
     * persists the state change and the removed characteristics.
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
