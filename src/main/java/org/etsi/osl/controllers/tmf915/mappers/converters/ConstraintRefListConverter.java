package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.ConstraintRef;

import java.util.List;

@Converter
public class ConstraintRefListConverter extends AbstractListJsonConverter<ConstraintRef> {
    @Override
    protected TypeReference<List<ConstraintRef>> typeReference() {
        return new TypeReference<>() {};
    }
}
