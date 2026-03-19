package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.CharacteristicSpecificationRelationship;

import java.util.List;

@Converter
public class CharacteristicSpecificationRelationshipListConverter extends AbstractListJsonConverter<CharacteristicSpecificationRelationship> {
    @Override
    protected TypeReference<List<CharacteristicSpecificationRelationship>> typeReference() {
        return new TypeReference<>() {};
    }
}
