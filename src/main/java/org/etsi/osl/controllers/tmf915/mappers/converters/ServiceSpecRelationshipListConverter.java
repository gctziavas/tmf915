package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.ServiceSpecRelationship;

import java.util.List;

@Converter
public class ServiceSpecRelationshipListConverter extends AbstractListJsonConverter<ServiceSpecRelationship> {
    @Override
    protected TypeReference<List<ServiceSpecRelationship>> typeReference() {
        return new TypeReference<>() {};
    }
}
