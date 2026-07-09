package com.paxtech.utime.platform.analytics.domain.services;

import com.paxtech.utime.platform.analytics.domain.model.aggregates.AnalyticsEvent;
import com.paxtech.utime.platform.analytics.domain.model.queries.EventTypeCount;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAllAnalyticsEventsQuery;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAnalyticsEventByIdQuery;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAnalyticsSummaryQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service for the analytics bounded context.
 */
public interface AnalyticsEventQueryService {
    List<AnalyticsEvent> handle(GetAllAnalyticsEventsQuery query);

    Optional<AnalyticsEvent> handle(GetAnalyticsEventByIdQuery query);

    List<EventTypeCount> handle(GetAnalyticsSummaryQuery query);
}
