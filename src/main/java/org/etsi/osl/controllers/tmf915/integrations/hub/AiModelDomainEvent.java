package org.etsi.osl.controllers.tmf915.integrations.hub;

import org.etsi.osl.controllers.tmf915.model.AiModel;

/**
 * Internal Spring application event signalling that an {@link AiModel} mutated
 * and external listeners (TMF Hub subscribers) should be notified.
 */
public record AiModelDomainEvent(AiModel aiModel, EventKind kind) {

    public enum EventKind {
        CREATE("AiModelCreateEvent"),
        STATE_CHANGE("AiModelStateChangeEvent"),
        ATTRIBUTE_VALUE_CHANGE("AiModelAttributeValueChangeEvent"),
        DELETE("AiModelDeleteEvent");

        private final String tmfEventType;

        EventKind(String tmfEventType) {
            this.tmfEventType = tmfEventType;
        }

        public String tmfEventType() {
            return tmfEventType;
        }
    }
}
