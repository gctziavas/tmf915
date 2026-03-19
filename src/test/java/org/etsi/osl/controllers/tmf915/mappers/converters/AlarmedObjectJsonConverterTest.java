package org.etsi.osl.controllers.tmf915.mappers.converters;

import org.etsi.osl.controllers.tmf915.model.AlarmedObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlarmedObjectJsonConverterTest {

    private AlarmedObjectJsonConverter converter;

    @BeforeEach
    void setUp() {
        converter = new AlarmedObjectJsonConverter();
    }

    @Test
    void convertToDatabaseColumn_withObject_returnsJson() {
        AlarmedObject obj = new AlarmedObject();
        obj.setId("ao-1");
        obj.setHref("http://example.com/object/1");

        String json = converter.convertToDatabaseColumn(obj);

        assertNotNull(json);
        assertTrue(json.contains("\"id\":\"ao-1\""));
        assertTrue(json.contains("\"href\":\"http://example.com/object/1\""));
    }

    @Test
    void convertToDatabaseColumn_null_returnsNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute_validJson_returnsObject() {
        String json = "{\"id\":\"ao-1\",\"href\":\"http://example.com/object/1\"}";

        AlarmedObject result = converter.convertToEntityAttribute(json);

        assertNotNull(result);
        assertEquals("ao-1", result.getId());
        assertEquals("http://example.com/object/1", result.getHref());
    }

    @Test
    void convertToEntityAttribute_null_returnsNull() {
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void convertToEntityAttribute_blank_returnsNull() {
        assertNull(converter.convertToEntityAttribute("  "));
    }

    @Test
    void convertToEntityAttribute_invalidJson_returnsNull() {
        assertNull(converter.convertToEntityAttribute("{broken"));
    }

    @Test
    void roundTrip_preservesData() {
        AlarmedObject original = new AlarmedObject();
        original.setId("ao-2");
        original.setHref("http://example.com/object/2");

        String json = converter.convertToDatabaseColumn(original);
        AlarmedObject restored = converter.convertToEntityAttribute(json);

        assertNotNull(restored);
        assertEquals("ao-2", restored.getId());
        assertEquals("http://example.com/object/2", restored.getHref());
    }
}
