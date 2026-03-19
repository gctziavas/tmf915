package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.EventSubscription;
import org.etsi.osl.controllers.tmf915.model.EventSubscriptionInput;

public final class EventSubscriptionMapper {

    private EventSubscriptionMapper() {}

    public static EventSubscription fromInput(EventSubscriptionInput src) {
        EventSubscription s = new EventSubscription();
        s.setCallback(src.getCallback());
        s.setQuery(src.getQuery());
        return s;
    }
}
