package com.paxtech.utime.platform.services.interfaces.rest.transform;

import com.paxtech.utime.platform.services.domain.model.commands.UpdateServiceCommand;
import com.paxtech.utime.platform.services.interfaces.rest.resources.UpdateServiceResource;

public class UpdateServiceCommandFromResourceAssembler {
    public static UpdateServiceCommand toCommandFromResource(Long id, UpdateServiceResource resource) {
        return new UpdateServiceCommand(
                id,
                resource.name(),
                resource.duration(),
                resource.price()
        );
    }
}
