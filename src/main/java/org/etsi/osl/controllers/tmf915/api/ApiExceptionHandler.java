package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.reposervices.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
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
}
