package org.etsi.osl.controllers.tmf915.artifacts;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;

/**
 * Platform-specific part of model downloads: finds where a specification's files are stored.
 * One implementation per model platform (MLflow, Kubeflow, …), chosen at runtime like
 * {@code PlatformDeployer}. Locators read the specification only; they never read storage.
 */
public interface ArtifactLocator {

    /**
     * Whether this locator handles the specification, typically decided by its {@code platform}
     * characteristic.
     */
    boolean supports(AiModelSpecification specification);

    /**
     * Returns the artifact's location and format.
     *
     * @throws ArtifactNotAvailableException if the specification has no artifact this locator can
     *                                       find (for example a missing location characteristic)
     */
    ArtifactLocation locate(AiModelSpecification specification);
}
