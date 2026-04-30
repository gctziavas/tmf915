package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.Hub;
import org.etsi.osl.controllers.tmf915.model.HubCreate;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class HubMapperUnitTest {

    @Test
    void fromCreate_allFieldsSet_mapsCorrectly() {
        HubCreate src = new HubCreate();
        src.setCallback(URI.create("http://hub.example.com/callback"));
        src.setQuery("topic=alarm");
        src.setAtBaseType("BaseEntity");
        src.setAtType("Hub");
        src.setAtSchemaLocation(URI.create("http://example.com/schema"));

        Hub result = HubMapper.fromCreate(src);

        assertEquals(URI.create("http://hub.example.com/callback"), result.getCallback());
        assertEquals("topic=alarm", result.getQuery());
        assertEquals("BaseEntity", result.getAtBaseType());
        assertEquals("Hub", result.getAtType());
        assertEquals(URI.create("http://example.com/schema"), result.getAtSchemaLocation());
    }

    @Test
    void fromCreate_nullFields_mapsNulls() {
        HubCreate src = new HubCreate();

        Hub result = HubMapper.fromCreate(src);

        assertNotNull(result);
        assertNull(result.getCallback());
        assertNull(result.getQuery());
    }
}
