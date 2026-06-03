package org.etsi.osl.controllers.tmf915.integrations.hub;

import org.etsi.osl.controllers.tmf915.api.EventsSubscriptionApiDelegate;
import org.etsi.osl.controllers.tmf915.model.EventSubscription;
import org.etsi.osl.controllers.tmf915.model.EventSubscriptionInput;
import org.etsi.osl.controllers.tmf915.repo.EventSubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of the TMF events subscription (Hub) API.
 *
 * <p>Persists callback URLs in MySQL via {@link EventSubscriptionRepository}.
 * Notification dispatch is handled asynchronously by {@link WebhookDispatcher}.
 */
@Service
public class EventsSubscriptionApiDelegateImpl implements EventsSubscriptionApiDelegate {

    private static final Logger log = LoggerFactory.getLogger(EventsSubscriptionApiDelegateImpl.class);

    private final EventSubscriptionRepository repository;

    public EventsSubscriptionApiDelegateImpl(EventSubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<EventSubscription> registerListener(EventSubscriptionInput data) {
        if (data == null || data.getCallback() == null || data.getCallback().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        EventSubscription subscription = new EventSubscription();
        subscription.setId(UUID.randomUUID().toString());
        subscription.setCallback(data.getCallback());
        subscription.setQuery(data.getQuery());

        EventSubscription saved = repository.save(subscription);
        log.info("Registered event listener {} -> {}", saved.getId(), saved.getCallback());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Override
    public ResponseEntity<Void> unregisterListener(String id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        log.info("Unregistered event listener {}", id);
        return ResponseEntity.noContent().build();
    }
}
