package com.paxtech.utime.platform.analytics.interfaces.rest.transform;

import com.paxtech.utime.platform.analytics.domain.model.aggregates.AnalyticsEvent;
import com.paxtech.utime.platform.analytics.interfaces.rest.resources.AnalyticsEventResource;

public class AnalyticsEventResourceFromEntityAssembler {
    public static AnalyticsEventResource toResourceFromEntity(AnalyticsEvent entity) {
        return new AnalyticsEventResource(
                entity.getId(),
                entity.getEventType(),
                entity.getActorType().name(),
                entity.getActorId(),
                entity.getProviderId(),
                entity.getReservationId(),
                entity.getMetadata(),
                entity.getOccurredAt().toString());
    }
}
