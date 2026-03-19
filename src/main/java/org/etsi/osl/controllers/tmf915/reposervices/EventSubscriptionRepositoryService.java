package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.EventSubscriptionMapper;
import org.etsi.osl.controllers.tmf915.model.EventSubscription;
import org.etsi.osl.controllers.tmf915.model.EventSubscriptionInput;
import org.etsi.osl.controllers.tmf915.repo.EventSubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventSubscriptionRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(EventSubscriptionRepositoryService.class);

    private final EventSubscriptionRepository eventSubscriptionRepository;

    public EventSubscriptionRepositoryService(EventSubscriptionRepository eventSubscriptionRepository) {
        this.eventSubscriptionRepository = eventSubscriptionRepository;
    }

    public EventSubscription registerListener(EventSubscriptionInput src) {
        log.info("EventSubscription REGISTER: {}", src);
        EventSubscription sub = EventSubscriptionMapper.fromInput(src);
        sub.setId(UUID.randomUUID().toString());
        return eventSubscriptionRepository.save(sub);
    }

    public void unregisterListener(String id) {
        log.info("EventSubscription UNREGISTER with ID: {}", id);
        EventSubscription sub = eventSubscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No EventSubscription with ID: " + id));
        eventSubscriptionRepository.delete(sub);
    }
}
