package org.etsi.osl.controllers.tmf915;

import org.junit.jupiter.api.Test;

import java.text.ParsePosition;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RFC3339DateFormatUnitTest {

    private final RFC3339DateFormat dateFormat = new RFC3339DateFormat();

    @Test
    void format_producesRFC3339UtcString() {
        // 2024-01-15T10:30:00.000Z
        Date date = new Date(1705314600000L);
        StringBuffer sb = new StringBuffer();
        StringBuffer result = dateFormat.format(date, sb, new java.text.FieldPosition(0));
        assertNotNull(result);
        String formatted = result.toString();
        // RFC3339 / ISO8601 format: contains T and timezone offset
        assertTrue(formatted.contains("T"), "Expected ISO8601 date-time separator 'T'");
    }

    @Test
    void parse_validRFC3339String_returnsDate() {
        String input = "2024-01-15T10:30:00.000Z";
        ParsePosition pos = new ParsePosition(0);
        Date result = dateFormat.parse(input, pos);
        assertNotNull(result);
        assertEquals(1705314600000L, result.getTime());
    }

    @Test
    void parse_validRFC3339StringWithOffset_returnsDate() {
        String input = "2024-01-15T11:30:00.000+01:00";
        ParsePosition pos = new ParsePosition(0);
        Date result = dateFormat.parse(input, pos);
        assertNotNull(result);
        assertEquals(1705314600000L, result.getTime());
    }

    @Test
    void parse_invalidString_returnsNull() {
        ParsePosition pos = new ParsePosition(0);
        Date result = dateFormat.parse("not-a-date", pos);
        assertNull(result);
    }

    @Test
    void clone_returnsSameInstance() {
        Object cloned = dateFormat.clone();
        assertSame(dateFormat, cloned);
    }

    @Test
    void formatAndParse_roundTrip() {
        Date original = new Date(1705314600000L);
        StringBuffer sb = new StringBuffer();
        String formatted = dateFormat.format(original, sb, new java.text.FieldPosition(0)).toString();

        ParsePosition pos = new ParsePosition(0);
        Date parsed = dateFormat.parse(formatted, pos);

        assertNotNull(parsed);
        assertEquals(original.getTime(), parsed.getTime());
    }
}
