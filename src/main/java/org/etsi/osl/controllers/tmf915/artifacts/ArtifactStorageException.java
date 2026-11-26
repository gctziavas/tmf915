package org.etsi.osl.controllers.tmf915.artifacts;

/** Artifact storage could not be read. Mapped to {@code 503}; details are only logged. */
public class ArtifactStorageException extends RuntimeException {

    public ArtifactStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
