package com.paxtech.utime.platform.analytics.domain.model.queries;

/**
 * Query to fetch a single analytics event by its id.
 *
 * @param eventId the analytics event id
 */
public record GetAnalyticsEventByIdQuery(Long eventId) {
}
