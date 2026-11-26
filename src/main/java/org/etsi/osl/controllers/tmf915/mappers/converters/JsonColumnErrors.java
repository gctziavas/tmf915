package org.etsi.osl.controllers.tmf915.mappers.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Failures of the JSON attribute converters (handoff 5.9).
 *
 * <p>The converters used to return an empty value when a column could not be parsed. The entity
 * then looked empty, and the next save wrote the empty value over the original data. They now
 * fail: a loud error on one row is better than silent data loss. Null and blank columns still
 * read as empty.
 */
final class JsonColumnErrors {

    private static final Logger log = LoggerFactory.getLogger(JsonColumnErrors.class);

    private JsonColumnErrors() {}

    /** Logs at ERROR and returns the exception to throw for a column that cannot be read. */
    static IllegalStateException unreadable(Class<?> converter, String dbData, Exception cause) {
        String start = dbData.length() <= 80 ? dbData : dbData.substring(0, 80) + "…";
        String message = converter.getSimpleName() + " cannot read a stored JSON value (" + dbData.length()
                + " characters, starting with: " + start + "): " + cause.getMessage();
        log.error("{}. The row was not changed; fix or remove the value in the database.", message);
        return new IllegalStateException(message, cause);
    }

    /** Returns the exception to throw for a value that cannot be written as JSON. */
    static IllegalStateException unwritable(Class<?> converter, Object value, Exception cause) {
        String message = converter.getSimpleName() + " cannot write a " + value.getClass().getSimpleName()
                + " as JSON: " + cause.getMessage();
        log.error(message);
        return new IllegalStateException(message, cause);
    }
}
