package com.paxtech.utime.platform.analytics.interfaces.rest.resources;

import java.util.List;

/**
 * Detailed analytics summary response for the analytics dashboard.
 */
public record AnalyticsSummaryResource(
        long totalEvents,
        long distinctActors,
        String firstEventAt,
        String lastEventAt,
        List<EventTypeSummaryResource> byEventType,
        List<ActorTypeSummaryResource> byActorType
) {
    public record EventTypeSummaryResource(
            String eventType,
            long total,
            long clientCount,
            long providerCount,
            long anonymousCount
    ) {
    }

    public record ActorTypeSummaryResource(String actorType, long total) {
    }
}
