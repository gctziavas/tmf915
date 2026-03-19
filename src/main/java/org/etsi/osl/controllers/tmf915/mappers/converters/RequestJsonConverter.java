package org.etsi.osl.controllers.tmf915.mappers.converters;

import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.Request;

@Converter
public class RequestJsonConverter extends AbstractTypedJsonConverter<Request> {
    public RequestJsonConverter() {
        super(Request.class);
    }
}
