package com.paxtech.utime.platform.analytics.domain.model.queries;

import com.paxtech.utime.platform.analytics.domain.model.valueobjects.ActorType;

/**
 * Query to list analytics events. All filters are optional (null means "any"),
 * so the analytics frontend can slice by event type, actor or provider.
 *
 * @param eventType  optional event name filter
 * @param actorType  optional actor type filter
 * @param actorId    optional actor id filter
 * @param providerId optional provider filter
 */
public record GetAllAnalyticsEventsQuery(
        String eventType,
        ActorType actorType,
        Long actorId,
        Long providerId
) {
    public static GetAllAnalyticsEventsQuery all() {
        return new GetAllAnalyticsEventsQuery(null, null, null, null);
    }
}
