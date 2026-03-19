package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.AlarmRef;

import java.util.List;

@Converter
public class AlarmRefListConverter extends AbstractListJsonConverter<AlarmRef> {
    @Override
    protected TypeReference<List<AlarmRef>> typeReference() {
        return new TypeReference<>() {};
    }
}
