package org.etsi.osl.controllers.tmf915.mappers.converters;

import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.CrossedThresholdInformation;

@Converter
public class CrossedThresholdInformationJsonConverter extends AbstractTypedJsonConverter<CrossedThresholdInformation> {
    public CrossedThresholdInformationJsonConverter() {
        super(CrossedThresholdInformation.class);
    }
}
