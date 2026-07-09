package com.paxtech.utime.platform.analytics.interfaces.rest.transform;

import com.paxtech.utime.platform.analytics.domain.model.queries.AnalyticsSummary;
import com.paxtech.utime.platform.analytics.interfaces.rest.resources.AnalyticsSummaryResource;
import com.paxtech.utime.platform.analytics.interfaces.rest.resources.AnalyticsSummaryResource.ActorTypeSummaryResource;
import com.paxtech.utime.platform.analytics.interfaces.rest.resources.AnalyticsSummaryResource.EventTypeSummaryResource;

public class AnalyticsSummaryResourceFromEntityAssembler {

    public static AnalyticsSummaryResource toResourceFromEntity(AnalyticsSummary summary) {
        var byEventType = summary.byEventType().stream()
                .map(e -> new EventTypeSummaryResource(
                        e.eventType(),
                        e.total(),
                        e.clientCount(),
                        e.providerCount(),
                        e.anonymousCount()))
                .toList();

        var byActorType = summary.byActorType().stream()
                .map(a -> new ActorTypeSummaryResource(a.actorType().name(), a.total()))
                .toList();

        return new AnalyticsSummaryResource(
                summary.totalEvents(),
                summary.distinctActors(),
                summary.firstEventAt() != null ? summary.firstEventAt().toString() : null,
                summary.lastEventAt() != null ? summary.lastEventAt().toString() : null,
                byEventType,
                byActorType);
    }
}
