package org.etsi.osl.controllers.tmf915.integrations.hub;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.EventSubscription;
import org.etsi.osl.controllers.tmf915.repo.EventSubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Asynchronously delivers TMF Hub notifications to all registered subscribers
 * whenever an {@link AiModelDomainEvent} is published.
 *
 * <p>Notifications follow the TMF event envelope shape:
 * <pre>{@code
 * {
 *   "eventId": "<uuid>",
 *   "eventTime": "<iso-8601>",
 *   "eventType": "AiModelCreateEvent" | "AiModelStateChangeEvent" | ...,
 *   "event": { "aiModel": { ... } }
 * }
 * }</pre>
 */
@Component
public class WebhookDispatcher {

    private static final Logger log = LoggerFactory.getLogger(WebhookDispatcher.class);

    private final EventSubscriptionRepository repository;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public WebhookDispatcher(EventSubscriptionRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder().build();
    }

    @Async
    @EventListener
    public void onDomainEvent(AiModelDomainEvent event) {
        Iterable<EventSubscription> subscriptions = repository.findAll();
        if (!subscriptions.iterator().hasNext()) {
            return;
        }

        Map<String, Object> payload = buildEnvelope(event);

        for (EventSubscription subscription : subscriptions) {
            if (!SubscriptionQueryMatcher.matches(subscription.getQuery(), event)) {
                log.debug("Skipping subscription {} (query='{}' does not match event {})",
                        subscription.getId(), subscription.getQuery(), event.kind().tmfEventType());
                continue;
            }
            deliver(subscription, payload);
        }
    }

    private Map<String, Object> buildEnvelope(AiModelDomainEvent event) {
        Map<String, Object> envelope = new HashMap<>();
        envelope.put("eventId", UUID.randomUUID().toString());
        envelope.put("eventTime", OffsetDateTime.now().toString());
        envelope.put("eventType", event.kind().tmfEventType());

        Map<String, Object> body = new HashMap<>();
        body.put("aiModel", event.aiModel());
        envelope.put("event", body);
        return envelope;
    }

    private void deliver(EventSubscription subscription, Map<String, Object> payload) {
        String callback = subscription.getCallback();
        try {
            restClient.post()
                    .uri(callback)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(objectMapper.writeValueAsString(payload))
                    .retrieve()
                    .toBodilessEntity();
            log.debug("Delivered event {} to {}", payload.get("eventType"), callback);
        } catch (Exception ex) {
            log.warn("Failed to deliver event to subscriber {} ({}): {}",
                    subscription.getId(), callback, ex.getMessage());
        }
    }

    /** Visible for tests. */
    Duration timeout() {
        return Duration.ofSeconds(5);
    }
}
