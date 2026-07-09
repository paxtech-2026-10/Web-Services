package com.paxtech.utime.platform.analytics.interfaces.rest.transform;

import com.paxtech.utime.platform.analytics.domain.model.commands.RecordAnalyticsEventCommand;
import com.paxtech.utime.platform.analytics.domain.model.valueobjects.ActorType;
import com.paxtech.utime.platform.analytics.interfaces.rest.resources.RecordAnalyticsEventResource;

public class RecordAnalyticsEventCommandFromResourceAssembler {
    public static RecordAnalyticsEventCommand toCommandFromResource(RecordAnalyticsEventResource resource) {
        return new RecordAnalyticsEventCommand(
                resource.eventType(),
                ActorType.fromString(resource.actorType()),
                resource.actorId(),
                resource.providerId(),
                resource.reservationId(),
                resource.metadata());
    }
}
