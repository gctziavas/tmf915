package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.ResourceSpecificationRef;

import java.util.List;

@Converter
public class ResourceSpecificationRefListConverter extends AbstractListJsonConverter<ResourceSpecificationRef> {
    @Override
    protected TypeReference<List<ResourceSpecificationRef>> typeReference() {
        return new TypeReference<>() {};
    }
}
