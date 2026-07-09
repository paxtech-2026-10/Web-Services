package com.paxtech.utime.platform.analytics.domain.services;

import com.paxtech.utime.platform.analytics.domain.model.aggregates.AnalyticsEvent;
import com.paxtech.utime.platform.analytics.domain.model.commands.RecordAnalyticsEventCommand;

/**
 * Command service for the analytics bounded context.
 */
public interface AnalyticsEventCommandService {
    /**
     * Records a new analytics event.
     *
     * @param command the {@link RecordAnalyticsEventCommand}
     * @return the persisted {@link AnalyticsEvent}
     */
    AnalyticsEvent handle(RecordAnalyticsEventCommand command);
}
