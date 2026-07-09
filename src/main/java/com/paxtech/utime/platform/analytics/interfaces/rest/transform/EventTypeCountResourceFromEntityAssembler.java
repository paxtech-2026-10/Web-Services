package com.paxtech.utime.platform.analytics.interfaces.rest.transform;

import com.paxtech.utime.platform.analytics.domain.model.queries.EventTypeCount;
import com.paxtech.utime.platform.analytics.interfaces.rest.resources.EventTypeCountResource;

public class EventTypeCountResourceFromEntityAssembler {
    public static EventTypeCountResource toResourceFromEntity(EventTypeCount entity) {
        return new EventTypeCountResource(entity.eventType(), entity.total());
    }
}
