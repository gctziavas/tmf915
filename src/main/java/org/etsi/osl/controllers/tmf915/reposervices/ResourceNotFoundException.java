package org.etsi.osl.controllers.tmf915.reposervices;

/**
 * A resource addressed by id does not exist. Mapped to {@code 404} by
 * {@code api.ApiExceptionHandler} (handoff 4.4).
 *
 * <p>Extends {@link IllegalArgumentException} so that code catching the broader type, which
 * was thrown for unknown ids before, keeps working.
 */
public class ResourceNotFoundException extends IllegalArgumentException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
