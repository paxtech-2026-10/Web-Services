package com.paxtech.utime.platform.workers.interfaces.rest.transform;

import com.paxtech.utime.platform.workers.domain.model.commands.CreateWorkerCommand;
import com.paxtech.utime.platform.workers.interfaces.rest.resources.CreateWorkerResource;

public class CreateWorkerCommandFromResourceAssembler {
    public static CreateWorkerCommand toCommandFromResource(CreateWorkerResource resource) {
        return new CreateWorkerCommand(
                resource.name(),
                resource.specialization(),
                resource.photoUrl(),
                resource.providerId()
        );
    }
}