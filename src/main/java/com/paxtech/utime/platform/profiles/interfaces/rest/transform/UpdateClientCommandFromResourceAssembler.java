package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateClientCommand;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.UpdateClientResource;

public class UpdateClientCommandFromResourceAssembler {
    public static UpdateClientCommand toCommandFromResource(Long id, UpdateClientResource resource) {
        return new UpdateClientCommand(id, resource.firstName(), resource.lastName(), resource.profileImageUrl());
    }
}








