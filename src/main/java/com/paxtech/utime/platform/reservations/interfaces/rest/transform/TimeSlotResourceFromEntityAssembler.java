package com.paxtech.utime.platform.reservations.interfaces.rest.transform;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.TimeSlot;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.TimeSlotResource;

public class TimeSlotResourceFromEntityAssembler {
    public static TimeSlotResource toResourceFromEntity(TimeSlot entity) {
        return new TimeSlotResource(
                entity.getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.isStatus(),
                entity.getType()
        );
    }
}
