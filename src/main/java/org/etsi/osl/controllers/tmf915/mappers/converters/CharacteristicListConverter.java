package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.Characteristic;

import java.util.List;

@Converter
public class CharacteristicListConverter extends AbstractListJsonConverter<Characteristic> {
    @Override
    protected TypeReference<List<Characteristic>> typeReference() {
        return new TypeReference<>() {};
    }
}
