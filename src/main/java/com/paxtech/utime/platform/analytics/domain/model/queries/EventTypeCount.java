package com.paxtech.utime.platform.analytics.domain.model.queries;

/**
 * Read model with the number of events recorded for a given event type.
 * <p>
 * {@code total} is a boxed {@link Long} on purpose: it maps the result of a
 * JPQL {@code COUNT(e)} (typed as {@link Long}) in the constructor expression
 * of {@code AnalyticsEventRepository.countGroupedByEventType()}. Using a
 * primitive {@code long} here can make Hibernate fail to resolve the
 * constructor at startup.
 *
 * @param eventType the event name
 * @param total     how many events of that type have been recorded
 */
public record EventTypeCount(String eventType, Long total) {
}
