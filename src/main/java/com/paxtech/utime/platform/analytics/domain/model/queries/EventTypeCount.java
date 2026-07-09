package com.paxtech.utime.platform.analytics.domain.model.queries;

/**
 * Read model with the number of events recorded for a given event type.
 *
 * @param eventType the event name
 * @param total     how many events of that type have been recorded
 */
public record EventTypeCount(String eventType, long total) {
}
