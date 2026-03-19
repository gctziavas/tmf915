package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.RelatedServiceOrderItem;

import java.util.List;

@Converter
public class RelatedServiceOrderItemListConverter extends AbstractListJsonConverter<RelatedServiceOrderItem> {
    @Override
    protected TypeReference<List<RelatedServiceOrderItem>> typeReference() {
        return new TypeReference<>() {};
    }
}
