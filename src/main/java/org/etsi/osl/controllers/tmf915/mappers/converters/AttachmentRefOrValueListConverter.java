package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.AttachmentRefOrValue;

import java.util.List;

@Converter
public class AttachmentRefOrValueListConverter extends AbstractListJsonConverter<AttachmentRefOrValue> {
    @Override
    protected TypeReference<List<AttachmentRefOrValue>> typeReference() {
        return new TypeReference<>() {};
    }
}
