package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateProviderCommand;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.UpdateProviderResource;

public class UpdateProviderCommandFromResourceAssembler {
    public static UpdateProviderCommand toCommandFromResource(Long id, UpdateProviderResource resource) {
        return new UpdateProviderCommand(id, resource.companyName());
    }
}




