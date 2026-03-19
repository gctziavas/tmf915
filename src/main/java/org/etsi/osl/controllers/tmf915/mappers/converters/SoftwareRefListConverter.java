package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.SoftwareRef;

import java.util.List;

@Converter
public class SoftwareRefListConverter extends AbstractListJsonConverter<SoftwareRef> {
    @Override
    protected TypeReference<List<SoftwareRef>> typeReference() {
        return new TypeReference<>() {};
    }
}
