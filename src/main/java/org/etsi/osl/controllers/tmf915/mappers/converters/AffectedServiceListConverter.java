package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.AffectedService;

import java.util.List;

@Converter
public class AffectedServiceListConverter extends AbstractListJsonConverter<AffectedService> {
    @Override
    protected TypeReference<List<AffectedService>> typeReference() {
        return new TypeReference<>() {};
    }
}
