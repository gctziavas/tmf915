package org.etsi.osl.controllers.tmf915.integrations.hub;

import org.etsi.osl.controllers.tmf915.model.AiModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Evaluates a TMF subscription {@code query} string against an outgoing event.
 *
 * <p>A query is an ampersand-separated list of {@code key=value} pairs. Each key
 * may carry multiple comma-separated accepted values. A subscription matches an
 * event only when <em>every</em> key in its query is satisfied.
 *
 * <p>Supported keys:
 * <ul>
 *   <li>{@code eventType} — TMF event type, e.g. {@code AiModelStateChangeEvent}</li>
 *   <li>{@code event.aiModel.id} — exact match on the AiModel UUID</li>
 *   <li>{@code event.aiModel.state} — TMF service state, e.g. {@code active}</li>
 * </ul>
 *
 * <p>An empty or {@code null} query matches every event (firehose mode).
 * Unknown keys cause the subscription to be skipped (fail-closed) so that
 * a typo in the consumer's query never silently turns into a firehose.
 */
final class SubscriptionQueryMatcher {

    private SubscriptionQueryMatcher() {
    }

    static boolean matches(String query, AiModelDomainEvent event) {
        if (query == null || query.isBlank()) {
            return true;
        }

        for (String pair : query.split("&")) {
            if (pair.isBlank()) {
                continue;
            }
            int eq = pair.indexOf('=');
            if (eq <= 0) {
                return false;
            }
            String key = pair.substring(0, eq).trim();
            String rawValue = pair.substring(eq + 1).trim();
            Set<String> accepted = new HashSet<>(Arrays.asList(rawValue.split(",")));

            String actual = extract(key, event);
            if (actual == null || !accepted.contains(actual)) {
                return false;
            }
        }
        return true;
    }

    private static String extract(String key, AiModelDomainEvent event) {
        AiModel aiModel = event.aiModel();
        return switch (key) {
            case "eventType" -> event.kind().tmfEventType();
            case "event.aiModel.id" -> aiModel == null ? null : aiModel.getId();
            case "event.aiModel.state" -> aiModel == null || aiModel.getState() == null
                    ? null
                    : aiModel.getState().getValue();
            default -> null;
        };
    }
}
