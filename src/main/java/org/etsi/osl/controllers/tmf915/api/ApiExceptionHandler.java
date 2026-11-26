package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.artifacts.ArtifactNotAvailableException;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactStorageException;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactTooLargeException;
import org.etsi.osl.controllers.tmf915.reposervices.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Error mapping for every controller in this package (handoff 4.4). The messages are written
 * for clients: an unknown id, or why a request was rejected. Any other exception is a 500
 * without a message ({@code spring.web.error.include-message: never}).
 *
 * <p>One advice instead of a handler per controller: springdoc 3 supports
 * {@code @ControllerAdvice}; 2.6 did not.
 */
@RestControllerAdvice(basePackageClasses = ApiExceptionHandler.class)
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /** Unknown id on GET, PATCH or DELETE: 404. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> notFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /** Invalid request (forbidden state change, disallowed dockerHost or callback, …): 400. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // Model downloads. The content type is set explicitly: the request may have asked for
    // application/zip, and the reason must not be labelled as a zip.

    /** No downloadable artifact (or unknown id on the download endpoint): 404. */
    @ExceptionHandler(ArtifactNotAvailableException.class)
    public ResponseEntity<String> artifactNotAvailable(ArtifactNotAvailableException ex) {
        return text(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** Above aimodel.artifacts.max-download-bytes: 413. */
    @ExceptionHandler(ArtifactTooLargeException.class)
    public ResponseEntity<String> artifactTooLarge(ArtifactTooLargeException ex) {
        return text(HttpStatus.CONTENT_TOO_LARGE, ex.getMessage());
    }

    /** Storage unreachable: 503. The cause (hosts, buckets) is logged, not returned. */
    @ExceptionHandler(ArtifactStorageException.class)
    public ResponseEntity<String> artifactStorage(ArtifactStorageException ex) {
        log.warn("Artifact storage error: {}", ex.getCause() == null ? ex.getMessage() : ex.getCause().toString());
        return text(HttpStatus.SERVICE_UNAVAILABLE, "Artifact storage is unavailable; try again later");
    }

    private static ResponseEntity<String> text(HttpStatus status, String body) {
        return ResponseEntity.status(status).contentType(MediaType.TEXT_PLAIN).body(body);
    }
}
