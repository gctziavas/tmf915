package org.etsi.osl.controllers.tmf915.mappers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;

import java.io.IOException;

/**
 * Serializes an AiModelSpecification relation as a slim TMF-style reference object.
 */
public class AiModelSpecificationRefSerializer extends JsonSerializer<AiModelSpecification> {

    @Override
    public void serialize(AiModelSpecification value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        gen.writeStartObject();
        writeString(gen, "id", value.getId());
        writeObject(gen, "href", value.getHref());
        writeString(gen, "name", value.getName());
        writeString(gen, "version", value.getVersion());
        writeString(gen, "@baseType", value.getAtBaseType());
        writeObject(gen, "@schemaLocation", value.getAtSchemaLocation());
        writeString(gen, "@type", value.getAtType());
        gen.writeEndObject();
    }

    private static void writeString(JsonGenerator gen, String fieldName, String value) throws IOException {
        if (value != null) {
            gen.writeStringField(fieldName, value);
        }
    }

    private static void writeObject(JsonGenerator gen, String fieldName, Object value) throws IOException {
        if (value != null) {
            gen.writeObjectField(fieldName, value);
        }
    }
}