package com.paxtech.utime.platform.analytics.interfaces.rest.resources;

import java.util.Map;

/**
 * Request body to record an analytics event.
 *
 * @param eventType     the event name (required), e.g. "profile_view"
 * @param actorType     "CLIENT", "PROVIDER" or "ANONYMOUS" (optional, defaults to ANONYMOUS)
 * @param actorId       id of the client/provider that triggered it (optional)
 * @param providerId    provider/salon the action relates to (optional)
 * @param reservationId reservation the action relates to (optional)
 * @param metadata      flexible key-value payload, e.g. {"has_reviews":"true","channel":"in_app"}
 */
public record RecordAnalyticsEventResource(
        String eventType,
        String actorType,
        Long actorId,
        Long providerId,
        Long reservationId,
        Map<String, String> metadata
) {
}
