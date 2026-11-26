package org.etsi.osl.controllers.tmf915.deployment;

import java.io.IOException;

/**
 * A deployment failure with a short reason that is safe to show to API users.
 *
 * <p>The scheduler stores only {@link #getPublicReason()} in the model's notes, which every API
 * user can read. The message and the cause, which can hold internal URLs, paths or command
 * output, go to the server log only (handoff 5.12).
 */
public class DeploymentException extends IOException {

    private final String publicReason;

    /**
     * @param publicReason short, client-safe description, e.g. "Image build failed"
     * @param cause        the underlying error, logged on the server only
     */
    public DeploymentException(String publicReason, Throwable cause) {
        super(publicReason + ": " + (cause != null ? cause.getMessage() : ""), cause);
        this.publicReason = publicReason;
    }

    public String getPublicReason() {
        return publicReason;
    }
}
