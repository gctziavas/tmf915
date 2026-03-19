package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.Note;

import java.util.List;

@Converter
public class NoteListConverter extends AbstractListJsonConverter<Note> {
    @Override
    protected TypeReference<List<Note>> typeReference() {
        return new TypeReference<>() {};
    }
}
