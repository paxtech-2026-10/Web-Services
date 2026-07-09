package com.paxtech.utime.platform.analytics.application.internal.commandservices;

import com.paxtech.utime.platform.analytics.domain.model.aggregates.AnalyticsEvent;
import com.paxtech.utime.platform.analytics.domain.model.commands.RecordAnalyticsEventCommand;
import com.paxtech.utime.platform.analytics.domain.services.AnalyticsEventCommandService;
import com.paxtech.utime.platform.analytics.infrastructure.persistence.jpa.repositories.AnalyticsEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalyticsEventCommandServiceImpl implements AnalyticsEventCommandService {

    private final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsEventCommandServiceImpl(AnalyticsEventRepository analyticsEventRepository) {
        this.analyticsEventRepository = analyticsEventRepository;
    }

    @Override
    @Transactional
    public AnalyticsEvent handle(RecordAnalyticsEventCommand command) {
        if (command.eventType() == null || command.eventType().isBlank()) {
            throw new IllegalArgumentException("eventType is required");
        }
        var event = new AnalyticsEvent(command);
        return analyticsEventRepository.save(event);
    }
}
