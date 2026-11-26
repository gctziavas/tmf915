package org.etsi.osl.controllers.tmf915.mappers.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectToJsonConverterUnitTest {

    private ObjectToJsonConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ObjectToJsonConverter();
    }

    @Test
    void convertToDatabaseColumn_withMap_returnsJson() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("key", "value");
        map.put("count", 42);

        String json = converter.convertToDatabaseColumn(map);

        assertNotNull(json);
        assertTrue(json.contains("\"key\":\"value\""));
        assertTrue(json.contains("\"count\":42"));
    }

    @Test
    void convertToDatabaseColumn_withString_returnsJsonString() {
        String json = converter.convertToDatabaseColumn("hello");

        assertEquals("\"hello\"", json);
    }

    @Test
    void convertToDatabaseColumn_null_returnsNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute_validJson_returnsObject() {
        String json = "{\"key\":\"value\"}";

        Object result = converter.convertToEntityAttribute(json);

        assertNotNull(result);
        assertInstanceOf(Map.class, result);
        assertEquals("value", ((Map<?, ?>) result).get("key"));
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
    void convertToEntityAttribute_invalidJson_failsInsteadOfReadingAsEmpty() {
        assertThrows(IllegalStateException.class, () -> converter.convertToEntityAttribute("{invalid"));
    }

    @Test
    void convertToDatabaseColumn_unserialisableValue_failsInsteadOfStoringNull() {
        // Before, the column was written as null, silently dropping the value.
        assertThrows(IllegalStateException.class, () -> converter.convertToDatabaseColumn(new Object()));
    }

    @Test
    void nullAndBlankColumns_stillReadAsEmpty() {
        assertNull(converter.convertToEntityAttribute(null));
        assertNull(converter.convertToEntityAttribute("  "));
    }
}
