package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.ServiceLevelSpecificationRef;

import java.util.List;

@Converter
public class ServiceLevelSpecificationRefListConverter extends AbstractListJsonConverter<ServiceLevelSpecificationRef> {
    @Override
    protected TypeReference<List<ServiceLevelSpecificationRef>> typeReference() {
        return new TypeReference<>() {};
    }
}
