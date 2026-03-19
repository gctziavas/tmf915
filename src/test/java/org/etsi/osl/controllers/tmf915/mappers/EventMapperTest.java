package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.Event;
import org.etsi.osl.controllers.tmf915.model.EventCreate;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    @Test
    void fromCreate_allFieldsSet_mapsCorrectly() {
        EventCreate src = new EventCreate();
        src.setCorrelationId("corr-1");
        src.setDescription("Test event");
        src.setDomain("network");
        src.setEventId("evt-123");
        src.setEventType("stateChange");
        src.setPriority("high");
        src.setTitle("Event Title");
        OffsetDateTime now = OffsetDateTime.now();
        src.setEventTime(now);
        src.setTimeOccurred(now);
        src.setAtBaseType("BaseEntity");
        src.setAtType("Event");
        src.setAtSchemaLocation(URI.create("http://example.com"));

        Event result = EventMapper.fromCreate(src);

        assertEquals("corr-1", result.getCorrelationId());
        assertEquals("Test event", result.getDescription());
        assertEquals("network", result.getDomain());
        assertEquals("evt-123", result.getEventId());
        assertEquals("stateChange", result.getEventType());
        assertEquals("high", result.getPriority());
        assertEquals("Event Title", result.getTitle());
        assertEquals(now, result.getEventTime());
        assertEquals(now, result.getTimeOccurred());
    }

    @Test
    void fromCreate_nullFields_mapsNulls() {
        EventCreate src = new EventCreate();

        Event result = EventMapper.fromCreate(src);

        assertNotNull(result);
        assertNull(result.getCorrelationId());
        assertNull(result.getEventId());
        assertNull(result.getTitle());
    }
}
