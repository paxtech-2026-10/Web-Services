package com.paxtech.utime.platform.reservations.interfaces.rest.transform;

import com.paxtech.utime.platform.reservations.domain.model.commands.CreateReservationCommand;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.CreateReservationResource;

public class CreateReservationCommandFromResourceAssembler {
    public static CreateReservationCommand toCommandFromResource(CreateReservationResource resource) {
        return new CreateReservationCommand(
                resource.clientId(),
                resource.providerId(),
                resource.paymentId(),
                resource.timeSlotId(),
                resource.workerId()
        );
    }
}
