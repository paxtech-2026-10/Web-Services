package com.paxtech.utime.platform.services.interfaces.rest.transform;

import com.paxtech.utime.platform.services.domain.model.commands.CreateServiceCommand;
import com.paxtech.utime.platform.services.interfaces.rest.resources.CreateServiceResource;

public class CreateServiceCommandFromResourceAssembler {
    public static CreateServiceCommand toCommandFromResource(CreateServiceResource resource) {
        return new CreateServiceCommand(
                resource.name(),
                resource.duration(),
                resource.price(),
                true,
                resource.providerId()
        );
    }
}
