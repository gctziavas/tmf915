package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.RelatedParty;

import java.util.List;

@Converter
public class RelatedPartyListConverter extends AbstractListJsonConverter<RelatedParty> {
    @Override
    protected TypeReference<List<RelatedParty>> typeReference() {
        return new TypeReference<>() {};
    }
}
