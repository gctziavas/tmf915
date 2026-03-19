package org.etsi.osl.controllers.tmf915.mappers.converters;

import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.Response;

@Converter
public class ResponseJsonConverter extends AbstractTypedJsonConverter<Response> {
    public ResponseJsonConverter() {
        super(Response.class);
    }
}
