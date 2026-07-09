package com.paxtech.utime.platform.analytics.interfaces.rest.resources;

import java.util.Map;

/**
 * Response body representing a recorded analytics event.
 */
public record AnalyticsEventResource(
        Long id,
        String eventType,
        String actorType,
        Long actorId,
        Long providerId,
        Long reservationId,
        Map<String, String> metadata,
        String occurredAt
) {
}
