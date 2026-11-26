package org.etsi.osl.controllers.tmf915.integrations.hub;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.etsi.osl.controllers.tmf915.model.EventSubscription;
import org.etsi.osl.controllers.tmf915.model.EventSubscriptionInput;
import org.etsi.osl.controllers.tmf915.repo.EventSubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** hub.webhook.enabled switches webhook delivery on and off. */
class WebhookEnablementTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            // As in the running application: lets @Value turn "2s" into a Duration.
            .withInitializer(context -> context.getBeanFactory()
                    .setConversionService(new org.springframework.boot.convert.ApplicationConversionService()))
            .withBean(EventSubscriptionRepository.class, () -> mock(EventSubscriptionRepository.class))
            .withBean(ObjectMapper.class, ObjectMapper::new)
            .withUserConfiguration(WebhookDispatcher.class);

    @Test
    void dispatcherIsOffByDefault() {
        runner.run(context -> assertThat(context).doesNotHaveBean(WebhookDispatcher.class));
    }

    @Test
    void dispatcherIsOffWhenDisabled() {
        runner.withPropertyValues("hub.webhook.enabled=false")
                .run(context -> assertThat(context).doesNotHaveBean(WebhookDispatcher.class));
    }

    @Test
    void dispatcherIsOnWhenEnabled() {
        runner.withPropertyValues("hub.webhook.enabled=true")
                .run(context -> assertThat(context).hasSingleBean(WebhookDispatcher.class));
    }

    @Test
    void registrationIsStillAcceptedWhenDisabled() {
        EventSubscriptionRepository repository = mock(EventSubscriptionRepository.class);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        EventsSubscriptionApiDelegateImpl delegate = new EventsSubscriptionApiDelegateImpl(repository, false);

        EventSubscriptionInput input = new EventSubscriptionInput();
        input.setCallback("http://subscriber.example/listener");
        ResponseEntity<EventSubscription> response = delegate.registerListener(input);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getCallback()).isEqualTo("http://subscriber.example/listener");
    }
}
