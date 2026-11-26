package org.etsi.osl.controllers.tmf915.artifacts;

/** The artifact exceeds {@code aimodel.artifacts.max-download-bytes}. Mapped to {@code 413}. */
public class ArtifactTooLargeException extends RuntimeException {

    public ArtifactTooLargeException(String message) {
        super(message);
    }
}
