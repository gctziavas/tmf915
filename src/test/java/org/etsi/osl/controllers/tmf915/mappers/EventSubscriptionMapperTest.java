package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.EventSubscription;
import org.etsi.osl.controllers.tmf915.model.EventSubscriptionInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventSubscriptionMapperTest {

    @Test
    void fromInput_allFieldsSet_mapsCorrectly() {
        EventSubscriptionInput src = new EventSubscriptionInput();
        src.setCallback("http://callback.example.com");
        src.setQuery("eventType=alarm");

        EventSubscription result = EventSubscriptionMapper.fromInput(src);

        assertEquals("http://callback.example.com", result.getCallback());
        assertEquals("eventType=alarm", result.getQuery());
    }

    @Test
    void fromInput_nullFields_mapsNulls() {
        EventSubscriptionInput src = new EventSubscriptionInput();

        EventSubscription result = EventSubscriptionMapper.fromInput(src);

        assertNotNull(result);
        assertNull(result.getCallback());
        assertNull(result.getQuery());
    }
}
