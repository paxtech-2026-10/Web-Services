package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Client;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.ClientResource;

public class ClientResourceFrontEntityAssembler {
    public static ClientResource toResourceFromEntity(Client entity) {
        return new ClientResource(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getUser().getId()
        );
    }
}
