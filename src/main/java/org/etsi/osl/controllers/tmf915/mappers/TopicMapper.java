package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.Topic;
import org.etsi.osl.controllers.tmf915.model.TopicCreate;

public final class TopicMapper {

    private TopicMapper() {}

    public static Topic fromCreate(TopicCreate src) {
        Topic t = new Topic();
        t.setContentQuery(src.getContentQuery());
        t.setHeaderQuery(src.getHeaderQuery());
        t.setName(src.getName());
        t.setAtBaseType(src.getAtBaseType());
        t.setAtSchemaLocation(src.getAtSchemaLocation());
        t.setAtType(src.getAtType());
        return t;
    }
}
