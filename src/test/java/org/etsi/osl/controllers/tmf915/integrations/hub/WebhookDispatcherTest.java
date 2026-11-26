package org.etsi.osl.controllers.tmf915.integrations.hub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.EventSubscription;
import org.etsi.osl.controllers.tmf915.repo.EventSubscriptionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

/**
 * Delivers to real local HTTP endpoints: one subscriber that never answers in time and one
 * that answers immediately.
 */
class WebhookDispatcherTest {

    private static final Duration READ_TIMEOUT = Duration.ofSeconds(1);

    private HttpServer server;
    private String baseUrl;
    private final CountDownLatch fastReceived = new CountDownLatch(1);
    private final AtomicInteger fastCount = new AtomicInteger();
    private final CountDownLatch releaseSlow = new CountDownLatch(1);
    private final java.util.Queue<String> fastBodies = new java.util.concurrent.ConcurrentLinkedQueue<>();

    private final EventSubscriptionRepository repository = mock(EventSubscriptionRepository.class);

    @BeforeEach
    void startServer() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.setExecutor(Executors.newCachedThreadPool());
        server.createContext("/slow", exchange -> {
            try {
                releaseSlow.await(30, TimeUnit.SECONDS); // far longer than the read timeout
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
        });
        server.createContext("/fast", exchange -> {
            fastBodies.add(new String(exchange.getRequestBody().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
            fastCount.incrementAndGet();
            fastReceived.countDown();
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
        });
        server.start();
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort();
    }

    @AfterEach
    void stopServer() {
        releaseSlow.countDown();
        server.stop(0);
    }

    private WebhookDispatcher dispatcher(ObjectMapper mapper, int threads, int queueCapacity) {
        // The subscribers here are on 127.0.0.1, which the default policy blocks on purpose.
        CallbackUrlPolicy allowLocal = new CallbackUrlPolicy("", false);
        return new WebhookDispatcher(repository, allowLocal, mapper, Duration.ofSeconds(1), READ_TIMEOUT, threads, queueCapacity);
    }

    private static AiModelDomainEvent event() {
        AiModel model = new AiModel();
        model.setId("m1");
        model.setName("model-1");
        return new AiModelDomainEvent(model, AiModelDomainEvent.EventKind.CREATE);
    }

    @Test
    void publishingReturnsImmediately_andSlowSubscriberDoesNotDelayOthers() throws Exception {
        // The slow subscriber is first: with sequential delivery the fast one would wait for it.
        when(repository.findAll()).thenReturn(List.of(
                new EventSubscription("s-slow", baseUrl + "/slow"),
                new EventSubscription("s-fast", baseUrl + "/fast")));
        WebhookDispatcher dispatcher = dispatcher(new ObjectMapper(), 4, 100);

        long start = System.nanoTime();
        dispatcher.onDomainEvent(event());
        long publishMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        assertTrue(publishMs < 200, "publishing blocked for " + publishMs + " ms");
        assertTrue(fastReceived.await(READ_TIMEOUT.toMillis() / 2, TimeUnit.MILLISECONDS),
                "fast subscriber was held up by the slow one");

        // The slow delivery is abandoned at the read timeout, so shutdown finishes well before
        // the slow endpoint would answer (30 s) or the shutdown grace period (10 s) ends.
        long shutdownStart = System.nanoTime();
        dispatcher.shutdown();
        long shutdownMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - shutdownStart);
        assertTrue(shutdownMs < READ_TIMEOUT.toMillis() + 2_000, "slow delivery not timed out: " + shutdownMs + " ms");
    }

    @Test
    void payloadIsSerialisedOncePerEvent() throws Exception {
        when(repository.findAll()).thenReturn(List.of(
                new EventSubscription("s1", baseUrl + "/fast"),
                new EventSubscription("s2", baseUrl + "/fast"),
                new EventSubscription("s3", baseUrl + "/fast")));
        ObjectMapper mapper = spy(new ObjectMapper());
        doReturn(mapper).when(mapper).copy(); // the dispatcher configures a copy; keep spying on it
        WebhookDispatcher dispatcher = dispatcher(mapper, 4, 100);

        dispatcher.onDomainEvent(event());
        long deadline = System.currentTimeMillis() + 5_000;
        while (fastCount.get() < 3 && System.currentTimeMillis() < deadline) {
            Thread.sleep(20);
        }

        assertTrue(fastCount.get() == 3, "expected 3 deliveries, got " + fastCount.get());
        verify(mapper, times(1)).writeValueAsString(any());
        dispatcher.shutdown();
    }

    @Test
    void modelWithDates_isDelivered_withIsoDates() throws Exception {
        when(repository.findAll()).thenReturn(List.of(new EventSubscription("s1", baseUrl + "/fast")));
        // The application's own ObjectMapper bean, as injected in production.
        WebhookDispatcher dispatcher = dispatcher(
                new ObjectMapper().registerModule(new org.openapitools.jackson.nullable.JsonNullableModule()), 4, 100);
        AiModel model = new AiModel();
        model.setId("m1");
        model.setStartDate(java.time.OffsetDateTime.parse("2026-11-26T10:00:00Z"));
        model.setEndDate(java.time.OffsetDateTime.parse("2026-11-27T10:00:00Z"));

        dispatcher.onDomainEvent(new AiModelDomainEvent(model, AiModelDomainEvent.EventKind.STATE_CHANGE));

        assertTrue(fastReceived.await(5, TimeUnit.SECONDS), "notification for a model with dates was not delivered");
        String body = fastBodies.peek();
        assertTrue(body.contains("\"startDate\":\"2026-11-26T10:00:00Z\""), body);
        assertTrue(body.contains("\"endDate\":\"2026-11-27T10:00:00Z\""), body);
        dispatcher.shutdown();
    }

    @Test
    void eventPublishedJustBeforeShutdown_isStillDelivered() throws Exception {
        when(repository.findAll()).thenReturn(List.of(
                new EventSubscription("s1", baseUrl + "/fast"),
                new EventSubscription("s2", baseUrl + "/fast")));
        WebhookDispatcher dispatcher = dispatcher(new ObjectMapper(), 4, 100);

        dispatcher.onDomainEvent(event());
        dispatcher.shutdown();

        assertTrue(fastCount.get() == 2, "expected 2 deliveries, got " + fastCount.get());
    }

    @Test
    void fullQueue_dropsNotificationsWithoutBlockingOrThrowing() throws Exception {
        when(repository.findAll()).thenReturn(List.of(new EventSubscription("s-slow", baseUrl + "/slow")));
        // One thread and room for one pending task: the rest must be dropped, not queued forever.
        WebhookDispatcher dispatcher = dispatcher(new ObjectMapper(), 1, 1);

        long start = System.nanoTime();
        for (int i = 0; i < 50; i++) {
            assertDoesNotThrow(() -> dispatcher.onDomainEvent(event()));
        }
        long publishMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        assertTrue(publishMs < 500, "publishing blocked for " + publishMs + " ms");

        releaseSlow.countDown();
        dispatcher.shutdown();
    }

    @Test
    void storedCallbackToAnInternalAddress_isNotDeliveredTo() throws Exception {
        // E.g. a subscription registered before callbacks were validated.
        when(repository.findAll()).thenReturn(List.of(new EventSubscription("s-internal", baseUrl + "/fast")));
        WebhookDispatcher dispatcher = new WebhookDispatcher(repository, new CallbackUrlPolicy("", true),
                new ObjectMapper(), Duration.ofSeconds(1), READ_TIMEOUT, 4, 100);

        dispatcher.onDomainEvent(event());
        dispatcher.shutdown();

        assertTrue(fastCount.get() == 0, "delivered to 127.0.0.1 despite the policy");
    }
}
