package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.ServiceRelationship;

import java.util.List;

@Converter
public class ServiceRelationshipListConverter extends AbstractListJsonConverter<ServiceRelationship> {
    @Override
    protected TypeReference<List<ServiceRelationship>> typeReference() {
        return new TypeReference<>() {};
    }
}
