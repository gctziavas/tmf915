package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.ServiceRefOrValue;

import java.util.List;

@Converter
public class ServiceRefOrValueListConverter extends AbstractListJsonConverter<ServiceRefOrValue> {
    @Override
    protected TypeReference<List<ServiceRefOrValue>> typeReference() {
        return new TypeReference<>() {};
    }
}
