package com.paxtech.utime.platform.analytics.domain.model.commands;

import com.paxtech.utime.platform.analytics.domain.model.valueobjects.ActorType;

import java.util.Map;

/**
 * Command to record a single analytics/tracking event.
 *
 * @param eventType     the event name (e.g. calendar_viewed, book_in_app_completed, profile_view)
 * @param actorType     who triggered the event (CLIENT, PROVIDER or ANONYMOUS)
 * @param actorId       id of the client/provider that triggered it (nullable)
 * @param providerId    provider/salon the action relates to (nullable)
 * @param reservationId reservation the action relates to (nullable)
 * @param metadata      flexible key-value payload (count, channel, has_reviews, ...)
 */
public record RecordAnalyticsEventCommand(
        String eventType,
        ActorType actorType,
        Long actorId,
        Long providerId,
        Long reservationId,
        Map<String, String> metadata
) {
}
