package org.etsi.osl.controllers.tmf915.mappers.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class UriToStringConverterUnitTest {

    private UriToStringConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UriToStringConverter();
    }

    @Test
    void convertToDatabaseColumn_withUri_returnsString() {
        URI uri = URI.create("http://example.com/path?q=1");

        String result = converter.convertToDatabaseColumn(uri);

        assertEquals("http://example.com/path?q=1", result);
    }

    @Test
    void convertToDatabaseColumn_null_returnsNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute_validString_returnsUri() {
        URI result = converter.convertToEntityAttribute("http://example.com/path");

        assertNotNull(result);
        assertEquals(URI.create("http://example.com/path"), result);
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
    void roundTrip_preservesUri() {
        URI original = URI.create("https://example.com/api/v1/resource");

        String dbValue = converter.convertToDatabaseColumn(original);
        URI restored = converter.convertToEntityAttribute(dbValue);

        assertEquals(original, restored);
    }
}
