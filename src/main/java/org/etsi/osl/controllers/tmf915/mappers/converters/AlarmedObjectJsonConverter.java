package org.etsi.osl.controllers.tmf915.mappers.converters;

import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.AlarmedObject;

@Converter
public class AlarmedObjectJsonConverter extends AbstractTypedJsonConverter<AlarmedObject> {
    public AlarmedObjectJsonConverter() {
        super(AlarmedObject.class);
    }
}
