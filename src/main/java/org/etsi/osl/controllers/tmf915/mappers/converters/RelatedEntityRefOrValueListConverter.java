package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.RelatedEntityRefOrValue;

import java.util.List;

@Converter
public class RelatedEntityRefOrValueListConverter extends AbstractListJsonConverter<RelatedEntityRefOrValue> {
    @Override
    protected TypeReference<List<RelatedEntityRefOrValue>> typeReference() {
        return new TypeReference<>() {};
    }
}
