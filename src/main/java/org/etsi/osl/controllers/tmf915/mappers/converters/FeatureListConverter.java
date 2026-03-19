package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.Feature;

import java.util.List;

@Converter
public class FeatureListConverter extends AbstractListJsonConverter<Feature> {
    @Override
    protected TypeReference<List<Feature>> typeReference() {
        return new TypeReference<>() {};
    }
}
