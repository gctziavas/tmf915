package org.etsi.osl.controllers.tmf915.mappers.converters;

import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharacteristicListConverterTest {

    private CharacteristicListConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CharacteristicListConverter();
    }

    @Test
    void convertToDatabaseColumn_withItems_returnsJsonArray() {
        Characteristic c = new Characteristic();
        c.setName("speed");
        c.setId("c-1");
        List<Characteristic> list = List.of(c);

        String json = converter.convertToDatabaseColumn(list);

        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"speed\""));
        assertTrue(json.contains("\"id\":\"c-1\""));
    }

    @Test
    void convertToDatabaseColumn_emptyList_returnsEmptyArray() {
        String json = converter.convertToDatabaseColumn(new ArrayList<>());
        assertEquals("[]", json);
    }

    @Test
    void convertToDatabaseColumn_null_returnsEmptyArray() {
        String json = converter.convertToDatabaseColumn(null);
        assertEquals("[]", json);
    }

    @Test
    void convertToEntityAttribute_validJson_returnsList() {
        String json = "[{\"name\":\"speed\",\"id\":\"c-1\"}]";

        List<Characteristic> result = converter.convertToEntityAttribute(json);

        assertEquals(1, result.size());
        assertEquals("speed", result.get(0).getName());
        assertEquals("c-1", result.get(0).getId());
    }

    @Test
    void convertToEntityAttribute_emptyString_returnsEmptyList() {
        List<Characteristic> result = converter.convertToEntityAttribute("");
        assertTrue(result.isEmpty());
    }

    @Test
    void convertToEntityAttribute_null_returnsEmptyList() {
        List<Characteristic> result = converter.convertToEntityAttribute(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertToEntityAttribute_invalidJson_returnsEmptyList() {
        List<Characteristic> result = converter.convertToEntityAttribute("{not valid json");
        assertTrue(result.isEmpty());
    }

    @Test
    void roundTrip_preservesData() {
        Characteristic c = new Characteristic();
        c.setName("accuracy");
        c.setId("c-2");
        List<Characteristic> original = List.of(c);

        String json = converter.convertToDatabaseColumn(original);
        List<Characteristic> restored = converter.convertToEntityAttribute(json);

        assertEquals(1, restored.size());
        assertEquals("accuracy", restored.get(0).getName());
        assertEquals("c-2", restored.get(0).getId());
    }
}
