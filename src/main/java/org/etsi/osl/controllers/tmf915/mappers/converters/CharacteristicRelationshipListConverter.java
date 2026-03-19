package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.CharacteristicRelationship;

import java.util.List;

@Converter
public class CharacteristicRelationshipListConverter extends AbstractListJsonConverter<CharacteristicRelationship> {
    @Override
    protected TypeReference<List<CharacteristicRelationship>> typeReference() {
        return new TypeReference<>() {};
    }
}
