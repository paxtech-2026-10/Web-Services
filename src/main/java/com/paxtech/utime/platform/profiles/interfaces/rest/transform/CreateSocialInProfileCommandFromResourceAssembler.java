package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateSocialInProfileCommand;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.CreateSocialInProfileResource;

public class CreateSocialInProfileCommandFromResourceAssembler {
    public static CreateSocialInProfileCommand toCommandFromResource(CreateSocialInProfileResource resource) {
        return new CreateSocialInProfileCommand(resource.providerProfileId(), resource.socialId());
    }
}
