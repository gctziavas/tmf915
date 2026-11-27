package org.etsi.osl.controllers.tmf915.artifacts;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;

import java.util.List;

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
     * Returns where each format the specification may offer is stored. The first is the primary
     * format, downloaded when the client names none. A listed format need not exist: formats
     * whose files are not stored are not offered ({@link ArtifactService} checks).
     *
     * @return at least one location
     * @throws ArtifactNotAvailableException if the specification has no artifact this locator can
     *                                       find (for example a missing location characteristic)
     */
    List<ArtifactLocation> locate(AiModelSpecification specification);
}
