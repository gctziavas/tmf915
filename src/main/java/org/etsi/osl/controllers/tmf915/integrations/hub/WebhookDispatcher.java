package org.etsi.osl.controllers.tmf915.integrations.hub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.EventSubscription;
import org.etsi.osl.controllers.tmf915.repo.EventSubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
// Off unless hub.webhook.enabled=true: no thread pool and no per-event subscription lookup.
@ConditionalOnProperty(name = "hub.webhook.enabled", havingValue = "true")
public class WebhookDispatcher {

    private static final Logger log = LoggerFactory.getLogger(WebhookDispatcher.class);

    private final EventSubscriptionRepository repository;
    private final CallbackUrlPolicy callbackPolicy;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;
    private final ThreadPoolExecutor executor;

    /**
     * @param connectTimeout max time to open a connection to a subscriber
     * @param readTimeout    max time to wait for a subscriber's response
     * @param threads        delivery threads; deliveries to different subscribers run in parallel
     * @param queueCapacity  pending deliveries kept when all threads are busy; beyond this,
     *                       notifications are dropped with a warning instead of blocking the
     *                       caller or growing memory without limit
     */
    public WebhookDispatcher(EventSubscriptionRepository repository,
                             CallbackUrlPolicy callbackPolicy,
                             ObjectMapper objectMapper,
                             @Value("${hub.webhook.connect-timeout:2s}") Duration connectTimeout,
                             @Value("${hub.webhook.read-timeout:5s}") Duration readTimeout,
                             @Value("${hub.webhook.threads:4}") int threads,
                             @Value("${hub.webhook.queue-capacity:1000}") int queueCapacity) {
        this.repository = repository;
        this.callbackPolicy = callbackPolicy;
        // The injected application ObjectMapper has no java.time support, so serialising an
        // AiModel with startDate/endDate failed and no notification was sent. A copy is
        // configured here (ISO-8601 strings, as in REST responses) to leave the shared bean as is.
        this.objectMapper = objectMapper.copy()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(
                HttpClient.newBuilder().connectTimeout(connectTimeout).build());
        requestFactory.setReadTimeout(readTimeout);
        this.restClient = RestClient.builder().requestFactory(requestFactory).build();

        AtomicInteger threadNumber = new AtomicInteger();
        this.executor = new ThreadPoolExecutor(
                threads, threads, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                runnable -> {
                    Thread thread = new Thread(runnable, "webhook-" + threadNumber.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                },
                (runnable, pool) -> {
                    if (pool.isShutdown()) {
                        // Shutting down: queued dispatch tasks still run and submit their
                        // deliveries; run those on the submitting thread so they are not lost.
                        runnable.run();
                    } else {
                        log.warn("Webhook delivery queue is full ({} pending); dropping a notification",
                                pool.getQueue().size());
                    }
                });
        this.executor.allowCoreThreadTimeOut(true);
    }

    /**
     * Returns immediately: looking up subscribers and delivering to them happens on the
     * dispatcher's own bounded pool, so the thread that published the event (usually an HTTP
     * request) is never held up by subscribers.
     */
    @EventListener
    public void onDomainEvent(AiModelDomainEvent event) {
        executor.execute(() -> dispatch(event));
    }

    private void dispatch(AiModelDomainEvent event) {
        Iterable<EventSubscription> subscriptions = repository.findAll();
        if (!subscriptions.iterator().hasNext()) {
            return;
        }

        Map<String, Object> payload = buildEnvelope(event);
        String eventType = event.kind().tmfEventType();
        String body = null;

        for (EventSubscription subscription : subscriptions) {
            if (!SubscriptionQueryMatcher.matches(subscription.getQuery(), event)) {
                log.debug("Skipping subscription {} (query='{}' does not match event {})",
                        subscription.getId(), subscription.getQuery(), eventType);
                continue;
            }
            if (body == null) {
                // Serialised once per event, only if at least one subscriber matches.
                try {
                    body = objectMapper.writeValueAsString(payload);
                } catch (Exception ex) {
                    log.warn("Failed to serialise {} notification for AiModel {}: {}",
                            eventType, event.aiModel() != null ? event.aiModel().getId() : null, ex.getMessage());
                    return;
                }
            }
            String json = body;
            // One task per subscriber: a slow or unreachable subscriber does not delay the others.
            executor.execute(() -> deliver(subscription, eventType, json));
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

    private void deliver(EventSubscription subscription, String eventType, String json) {
        String callback = subscription.getCallback();
        try {
            // Checked again here: the subscription may predate the policy, or DNS may have changed.
            callbackPolicy.check(callback);
        } catch (IllegalArgumentException rejected) {
            log.warn("Not delivering to subscriber {}: {}", subscription.getId(), rejected.getMessage());
            return;
        }
        try {
            restClient.post()
                    .uri(callback)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(json)
                    .retrieve()
                    .toBodilessEntity();
            log.debug("Delivered event {} to {}", eventType, callback);
        } catch (Exception ex) {
            log.warn("Failed to deliver event to subscriber {} ({}): {}",
                    subscription.getId(), callback, ex.getMessage());
        }
    }

    @PreDestroy
    void shutdown() throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }
    }
}
