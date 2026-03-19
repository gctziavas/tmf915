package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.Topic;
import org.etsi.osl.controllers.tmf915.model.TopicCreate;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class TopicMapperTest {

    @Test
    void fromCreate_allFieldsSet_mapsCorrectly() {
        TopicCreate src = new TopicCreate();
        src.setName("Topic-1");
        src.setContentQuery("type=alarm");
        src.setHeaderQuery("source=network");
        src.setAtBaseType("BaseEntity");
        src.setAtType("Topic");
        src.setAtSchemaLocation(URI.create("http://example.com"));

        Topic result = TopicMapper.fromCreate(src);

        assertEquals("Topic-1", result.getName());
        assertEquals("type=alarm", result.getContentQuery());
        assertEquals("source=network", result.getHeaderQuery());
        assertEquals("BaseEntity", result.getAtBaseType());
        assertEquals("Topic", result.getAtType());
    }

    @Test
    void fromCreate_nullFields_mapsNulls() {
        TopicCreate src = new TopicCreate();

        Topic result = TopicMapper.fromCreate(src);

        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getContentQuery());
    }
}
