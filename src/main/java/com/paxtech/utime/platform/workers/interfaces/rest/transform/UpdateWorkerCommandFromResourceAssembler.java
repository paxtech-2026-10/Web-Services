package com.paxtech.utime.platform.workers.interfaces.rest.transform;

import com.paxtech.utime.platform.workers.domain.model.commands.UpdateWorkerCommand;
import com.paxtech.utime.platform.workers.interfaces.rest.resources.UpdateWorkerResource;

public class UpdateWorkerCommandFromResourceAssembler {
    public static UpdateWorkerCommand toCommandFromResource(Long id, UpdateWorkerResource resource) {
        return new UpdateWorkerCommand(id, resource.name(), resource.specialization(), resource.photoUrl(), resource.providerId());
    }
}
