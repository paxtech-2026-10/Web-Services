package com.paxtech.utime.platform.analytics.application.internal.queryservices;

import com.paxtech.utime.platform.analytics.domain.model.aggregates.AnalyticsEvent;
import com.paxtech.utime.platform.analytics.domain.model.queries.EventTypeCount;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAllAnalyticsEventsQuery;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAnalyticsEventByIdQuery;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAnalyticsSummaryQuery;
import com.paxtech.utime.platform.analytics.domain.services.AnalyticsEventQueryService;
import com.paxtech.utime.platform.analytics.infrastructure.persistence.jpa.repositories.AnalyticsEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<EventTypeCount> handle(GetAnalyticsSummaryQuery query) {
        return analyticsEventRepository.countGroupedByEventType();
    }
}
