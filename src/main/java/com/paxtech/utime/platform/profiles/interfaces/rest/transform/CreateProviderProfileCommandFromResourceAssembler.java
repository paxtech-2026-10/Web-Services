package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderProfileCommand;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.CreateProviderProfileResource;

public class CreateProviderProfileCommandFromResourceAssembler {
    public static CreateProviderProfileCommand toCommandFromResource(CreateProviderProfileResource resource) {
        return new CreateProviderProfileCommand(
                resource.profileUrl(), 
                resource.coverUrl(), 
                resource.location(), 
                resource.providerId(),
                resource.description(),
                resource.openTime(),
                resource.closeTime()
        );
    }
}
