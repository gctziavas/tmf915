package org.etsi.osl.controllers.tmf915.integrations.hub;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SubscriptionQueryMatcherTest {

    private static AiModelDomainEvent event(String id, ServiceStateType state,
                                            AiModelDomainEvent.EventKind kind) {
        AiModel model = new AiModel();
        model.setId(id);
        model.setState(state);
        return new AiModelDomainEvent(model, kind);
    }

    @Test
    void emptyQueryMatchesEverything() {
        AiModelDomainEvent ev = event("abc", ServiceStateType.ACTIVE,
                AiModelDomainEvent.EventKind.STATE_CHANGE);
        assertThat(SubscriptionQueryMatcher.matches(null, ev)).isTrue();
        assertThat(SubscriptionQueryMatcher.matches("", ev)).isTrue();
        assertThat(SubscriptionQueryMatcher.matches("   ", ev)).isTrue();
    }

    @Test
    void filtersByEventType() {
        AiModelDomainEvent stateChange = event("abc", ServiceStateType.ACTIVE,
                AiModelDomainEvent.EventKind.STATE_CHANGE);
        AiModelDomainEvent create = event("abc", ServiceStateType.RESERVED,
                AiModelDomainEvent.EventKind.CREATE);

        String q = "eventType=AiModelStateChangeEvent";
        assertThat(SubscriptionQueryMatcher.matches(q, stateChange)).isTrue();
        assertThat(SubscriptionQueryMatcher.matches(q, create)).isFalse();
    }

    @Test
    void filtersByAiModelId() {
        AiModelDomainEvent target = event("abc-123", ServiceStateType.ACTIVE,
                AiModelDomainEvent.EventKind.STATE_CHANGE);
        AiModelDomainEvent other = event("zzz-999", ServiceStateType.ACTIVE,
                AiModelDomainEvent.EventKind.STATE_CHANGE);

        String q = "event.aiModel.id=abc-123";
        assertThat(SubscriptionQueryMatcher.matches(q, target)).isTrue();
        assertThat(SubscriptionQueryMatcher.matches(q, other)).isFalse();
    }

    @Test
    void multipleAcceptedValuesViaComma() {
        String q = "eventType=AiModelStateChangeEvent,AiModelDeleteEvent";
        assertThat(SubscriptionQueryMatcher.matches(q,
                event("a", null, AiModelDomainEvent.EventKind.STATE_CHANGE))).isTrue();
        assertThat(SubscriptionQueryMatcher.matches(q,
                event("a", null, AiModelDomainEvent.EventKind.DELETE))).isTrue();
        assertThat(SubscriptionQueryMatcher.matches(q,
                event("a", null, AiModelDomainEvent.EventKind.CREATE))).isFalse();
    }

    @Test
    void andCombinesPredicates() {
        String q = "eventType=AiModelStateChangeEvent&event.aiModel.id=abc";
        assertThat(SubscriptionQueryMatcher.matches(q,
                event("abc", ServiceStateType.ACTIVE,
                        AiModelDomainEvent.EventKind.STATE_CHANGE))).isTrue();
        assertThat(SubscriptionQueryMatcher.matches(q,
                event("xyz", ServiceStateType.ACTIVE,
                        AiModelDomainEvent.EventKind.STATE_CHANGE))).isFalse();
        assertThat(SubscriptionQueryMatcher.matches(q,
                event("abc", ServiceStateType.RESERVED,
                        AiModelDomainEvent.EventKind.CREATE))).isFalse();
    }

    @Test
    void unknownKeyFailsClosed() {
        AiModelDomainEvent ev = event("abc", ServiceStateType.ACTIVE,
                AiModelDomainEvent.EventKind.STATE_CHANGE);
        assertThat(SubscriptionQueryMatcher.matches("foo=bar", ev)).isFalse();
    }

    @Test
    void filtersByState() {
        String q = "event.aiModel.state=active";
        assertThat(SubscriptionQueryMatcher.matches(q,
                event("a", ServiceStateType.ACTIVE,
                        AiModelDomainEvent.EventKind.STATE_CHANGE))).isTrue();
        assertThat(SubscriptionQueryMatcher.matches(q,
                event("a", ServiceStateType.RESERVED,
                        AiModelDomainEvent.EventKind.STATE_CHANGE))).isFalse();
    }
}
