package com.paxtech.utime.platform.reservations.interfaces.rest.transform;

import com.paxtech.utime.platform.reservations.domain.model.commands.CreateTimeSlotCommand;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.CreateTimeSlotResource;

public class CreateTimeSlotCommandFromResourceAssembler {
    public static CreateTimeSlotCommand toCommandFromResource(CreateTimeSlotResource resource) {
        return new CreateTimeSlotCommand(
                resource.startTime(),
                resource.endTime(),
                resource.status(),
                resource.type()
        );
    }
}
