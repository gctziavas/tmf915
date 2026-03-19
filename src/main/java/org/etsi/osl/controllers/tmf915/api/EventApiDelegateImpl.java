package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.Event;
import org.etsi.osl.controllers.tmf915.model.EventCreate;
import org.etsi.osl.controllers.tmf915.reposervices.EventRepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public class EventApiDelegateImpl implements EventApiDelegate {

    private final EventRepositoryService eventRepositoryService;

    public EventApiDelegateImpl(EventRepositoryService eventRepositoryService) {
        this.eventRepositoryService = eventRepositoryService;
    }

    @Override
    public ResponseEntity<Event> createEvent(String topicId, EventCreate event) {
        Event created = eventRepositoryService.createEvent(topicId, event);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<List<Event>> listEvent(String topicId, String fields, Integer offset, Integer limit) {
        return ResponseEntity.ok(eventRepositoryService.findEventsByTopicId(topicId));
    }

    @Override
    public ResponseEntity<Event> retrieveEvent(String topicId, String id, String fields) {
        Event event = eventRepositoryService.findEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }
}
