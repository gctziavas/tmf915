package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.RelatedPlaceRefOrValue;

import java.util.List;

@Converter
public class RelatedPlaceRefOrValueListConverter extends AbstractListJsonConverter<RelatedPlaceRefOrValue> {
    @Override
    protected TypeReference<List<RelatedPlaceRefOrValue>> typeReference() {
        return new TypeReference<>() {};
    }
}
