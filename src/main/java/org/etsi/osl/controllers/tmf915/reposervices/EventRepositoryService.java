package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.EventMapper;
import org.etsi.osl.controllers.tmf915.model.Event;
import org.etsi.osl.controllers.tmf915.model.EventCreate;
import org.etsi.osl.controllers.tmf915.repo.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(EventRepositoryService.class);

    private final EventRepository eventRepository;

    public EventRepositoryService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findEventsByTopicId(String topicId) {
        log.info("Events LIST for topicId: {}", topicId);
        return eventRepository.findByTopicId(topicId);
    }

    public Event findEventById(String id) {
        log.info("Event FIND BY ID: {}", id);
        return eventRepository.findById(id).orElse(null);
    }

    public Event createEvent(String topicId, EventCreate src) {
        log.info("Event CREATE for topicId: {}", topicId);
        Event event = EventMapper.fromCreate(src);
        event.setId(UUID.randomUUID().toString());
        event.setTopicId(topicId);
        return eventRepository.save(event);
    }
}
