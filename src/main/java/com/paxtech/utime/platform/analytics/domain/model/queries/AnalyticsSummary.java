package com.paxtech.utime.platform.analytics.domain.model.queries;

import com.paxtech.utime.platform.analytics.domain.model.valueobjects.ActorType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Detailed analytics summary used to feed the analytics dashboard.
 *
 * @param totalEvents    total number of recorded events
 * @param distinctActors number of distinct (non-null) actors that fired events
 * @param firstEventAt   timestamp of the earliest event (null if none)
 * @param lastEventAt    timestamp of the latest event (null if none)
 * @param byEventType    per-event-type totals with a breakdown by actor type
 * @param byActorType    totals grouped by actor type
 */
public record AnalyticsSummary(
        long totalEvents,
        long distinctActors,
        LocalDateTime firstEventAt,
        LocalDateTime lastEventAt,
        List<EventTypeSummary> byEventType,
        List<ActorTypeSummary> byActorType
) {
    /** Per-event-type totals split by actor type. */
    public record EventTypeSummary(
            String eventType,
            long total,
            long clientCount,
            long providerCount,
            long anonymousCount
    ) {
    }

    /** Total number of events fired by a given actor type. */
    public record ActorTypeSummary(ActorType actorType, long total) {
    }
}
