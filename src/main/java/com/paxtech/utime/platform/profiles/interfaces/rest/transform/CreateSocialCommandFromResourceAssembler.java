package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateSocialCommand;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.CreateSocialResource;

public class CreateSocialCommandFromResourceAssembler {
    public static CreateSocialCommand toCommandFromResource(CreateSocialResource resource){
        return new CreateSocialCommand(resource.socialUrl(), resource.socialIcon());
    }
}
