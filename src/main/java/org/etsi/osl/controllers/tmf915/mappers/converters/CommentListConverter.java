package org.etsi.osl.controllers.tmf915.mappers.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.etsi.osl.controllers.tmf915.model.Comment;

import java.util.List;

@Converter
public class CommentListConverter extends AbstractListJsonConverter<Comment> {
    @Override
    protected TypeReference<List<Comment>> typeReference() {
        return new TypeReference<>() {};
    }
}
