package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderCommand;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.CreateProviderResource;

public class CreateProviderCommandFromResourceAssembler {
    public static CreateProviderCommand toCommandFromResource(CreateProviderResource resource) {
        return new CreateProviderCommand(
                resource.companyName(),
                resource.userId()
        );
    }
}
