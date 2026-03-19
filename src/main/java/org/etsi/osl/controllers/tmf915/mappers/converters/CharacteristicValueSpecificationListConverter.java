package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.CharacteristicValueSpecification;

import java.util.List;

@Converter
public class CharacteristicValueSpecificationListConverter extends AbstractListJsonConverter<CharacteristicValueSpecification> {
    @Override
    protected TypeReference<List<CharacteristicValueSpecification>> typeReference() {
        return new TypeReference<>() {};
    }
}
