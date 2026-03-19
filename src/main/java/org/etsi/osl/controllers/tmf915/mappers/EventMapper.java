package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.Event;
import org.etsi.osl.controllers.tmf915.model.EventCreate;

public final class EventMapper {

    private EventMapper() {}

    public static Event fromCreate(EventCreate src) {
        Event e = new Event();
        e.setCorrelationId(src.getCorrelationId());
        e.setDescription(src.getDescription());
        e.setDomain(src.getDomain());
        e.setEventId(src.getEventId());
        e.setEventTime(src.getEventTime());
        e.setEventType(src.getEventType());
        e.setPriority(src.getPriority());
        e.setTimeOccurred(src.getTimeOccurred());
        e.setTitle(src.getTitle());
        e.setAnalyticCharacteristic(src.getAnalyticCharacteristic());
        e.setEvent(src.getEvent());
        e.setRelatedParty(src.getRelatedParty());
        e.setReportingSystem(src.getReportingSystem());
        e.setSource(src.getSource());
        e.setAtBaseType(src.getAtBaseType());
        e.setAtSchemaLocation(src.getAtSchemaLocation());
        e.setAtType(src.getAtType());
        return e;
    }
}
