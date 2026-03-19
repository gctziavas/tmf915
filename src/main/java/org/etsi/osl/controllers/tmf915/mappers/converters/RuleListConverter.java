package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.Rule;

import java.util.List;

@Converter
public class RuleListConverter extends AbstractListJsonConverter<Rule> {
    @Override
    protected TypeReference<List<Rule>> typeReference() {
        return new TypeReference<>() {};
    }
}
