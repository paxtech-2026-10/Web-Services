package com.paxtech.utime.platform.analytics.domain.model.queries;

import java.time.LocalDateTime;

/**
 * Earliest and latest {@code occurredAt} across all recorded analytics events.
 * Both fields are null when there are no events yet.
 */
public record AnalyticsTimeRange(LocalDateTime first, LocalDateTime last) {
}
