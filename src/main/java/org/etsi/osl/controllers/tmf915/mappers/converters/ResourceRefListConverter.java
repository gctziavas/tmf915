package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.ResourceRef;

import java.util.List;

@Converter
public class ResourceRefListConverter extends AbstractListJsonConverter<ResourceRef> {
    @Override
    protected TypeReference<List<ResourceRef>> typeReference() {
        return new TypeReference<>() {};
    }
}
