package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.ProviderResource;

public class ProviderResourceFromEntityAssembler {
    public static ProviderResource toResourceFromEntity(Provider entity) {
        return new ProviderResource(
                entity.getId(),
                entity.getCompanyName(),
                entity.getUser().getId()
        );
    }
}
