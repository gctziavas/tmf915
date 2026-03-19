package org.etsi.osl.controllers.tmf915.mappers.converters;

import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.Violation;

@Converter
public class ViolationJsonConverter extends AbstractTypedJsonConverter<Violation> {
    public ViolationJsonConverter() {
        super(Violation.class);
    }
}
