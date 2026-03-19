package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification;

import java.util.List;

@Converter
public class CharacteristicSpecificationListConverter extends AbstractListJsonConverter<CharacteristicSpecification> {
    @Override
    protected TypeReference<List<CharacteristicSpecification>> typeReference() {
        return new TypeReference<>() {};
    }
}
