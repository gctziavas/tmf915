package org.etsi.osl.controllers.tmf915.mappers.converters;

import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.TemplateRef;

@Converter
public class TemplateRefJsonConverter extends AbstractTypedJsonConverter<TemplateRef> {
    public TemplateRefJsonConverter() {
        super(TemplateRef.class);
    }
}
