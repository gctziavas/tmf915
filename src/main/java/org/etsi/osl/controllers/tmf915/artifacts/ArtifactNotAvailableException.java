package org.etsi.osl.controllers.tmf915.artifacts;

/**
 * The specification exists but offers no download. Mapped to {@code 404} with the message as
 * the reason, so the message must be safe for clients: it never says whether a location outside
 * the allow-list exists.
 */
public class ArtifactNotAvailableException extends RuntimeException {

    public ArtifactNotAvailableException(String message) {
        super(message);
    }
}
