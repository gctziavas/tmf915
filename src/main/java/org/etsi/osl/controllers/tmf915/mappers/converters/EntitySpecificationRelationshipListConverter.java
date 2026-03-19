package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.EntitySpecificationRelationship;

import java.util.List;

@Converter
public class EntitySpecificationRelationshipListConverter extends AbstractListJsonConverter<EntitySpecificationRelationship> {
    @Override
    protected TypeReference<List<EntitySpecificationRelationship>> typeReference() {
        return new TypeReference<>() {};
    }
}
