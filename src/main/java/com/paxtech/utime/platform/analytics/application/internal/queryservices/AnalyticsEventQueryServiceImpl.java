package com.paxtech.utime.platform.analytics.application.internal.queryservices;

import com.paxtech.utime.platform.analytics.domain.model.aggregates.AnalyticsEvent;
import com.paxtech.utime.platform.analytics.domain.model.queries.AnalyticsSummary;
import com.paxtech.utime.platform.analytics.domain.model.queries.AnalyticsSummary.ActorTypeSummary;
import com.paxtech.utime.platform.analytics.domain.model.queries.AnalyticsSummary.EventTypeSummary;
import com.paxtech.utime.platform.analytics.domain.model.queries.AnalyticsTimeRange;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAllAnalyticsEventsQuery;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAnalyticsEventByIdQuery;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAnalyticsSummaryQuery;
import com.paxtech.utime.platform.analytics.domain.model.valueobjects.ActorType;
import com.paxtech.utime.platform.analytics.domain.services.AnalyticsEventQueryService;
import com.paxtech.utime.platform.analytics.infrastructure.persistence.jpa.repositories.AnalyticsEventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AnalyticsEventQueryServiceImpl implements AnalyticsEventQueryService {

    private final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsEventQueryServiceImpl(AnalyticsEventRepository analyticsEventRepository) {
        this.analyticsEventRepository = analyticsEventRepository;
    }

    @Override
    public List<AnalyticsEvent> handle(GetAllAnalyticsEventsQuery query) {
        return analyticsEventRepository.findAllByOrderByOccurredAtDesc().stream()
                .filter(e -> query.eventType() == null || query.eventType().equalsIgnoreCase(e.getEventType()))
                .filter(e -> query.actorType() == null || query.actorType() == e.getActorType())
                .filter(e -> query.actorId() == null || query.actorId().equals(e.getActorId()))
                .filter(e -> query.providerId() == null || query.providerId().equals(e.getProviderId()))
                .toList();
    }

    @Override
    public Optional<AnalyticsEvent> handle(GetAnalyticsEventByIdQuery query) {
        return analyticsEventRepository.findById(query.eventId());
    }

    @Override
    public AnalyticsSummary handle(GetAnalyticsSummaryQuery query) {
        var rows = analyticsEventRepository.countByEventTypeAndActor();

        // Acumular por tipo de evento (con desglose por actor) y por tipo de actor.
        Map<String, long[]> perType = new LinkedHashMap<>(); // [total, client, provider, anonymous]
        Map<ActorType, Long> perActor = new EnumMap<>(ActorType.class);
        long totalEvents = 0;

        for (var row : rows) {
            long count = row.total() != null ? row.total() : 0L;
            totalEvents += count;

            var buckets = perType.computeIfAbsent(row.eventType(), k -> new long[4]);
            buckets[0] += count;
            switch (row.actorType()) {
                case CLIENT -> buckets[1] += count;
                case PROVIDER -> buckets[2] += count;
                case ANONYMOUS -> buckets[3] += count;
            }
            perActor.merge(row.actorType(), count, Long::sum);
        }

        List<EventTypeSummary> byEventType = new ArrayList<>();
        perType.forEach((eventType, b) ->
                byEventType.add(new EventTypeSummary(eventType, b[0], b[1], b[2], b[3])));
        byEventType.sort(Comparator.comparingLong(EventTypeSummary::total).reversed());

        List<ActorTypeSummary> byActorType = new ArrayList<>();
        perActor.forEach((actorType, total) -> byActorType.add(new ActorTypeSummary(actorType, total)));

        AnalyticsTimeRange range = analyticsEventRepository.findTimeRange();
        long distinctActors = analyticsEventRepository.countDistinctActors();

        return new AnalyticsSummary(
                totalEvents,
                distinctActors,
                range != null ? range.first() : null,
                range != null ? range.last() : null,
                byEventType,
                byActorType);
    }
}
