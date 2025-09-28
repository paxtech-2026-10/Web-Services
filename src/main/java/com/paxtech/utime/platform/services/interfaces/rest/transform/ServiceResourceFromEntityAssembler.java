package com.paxtech.utime.platform.services.interfaces.rest.transform;

import com.paxtech.utime.platform.services.domain.model.aggregates.Service;
import com.paxtech.utime.platform.services.interfaces.rest.resources.ServiceResource;

public class ServiceResourceFromEntityAssembler {
    public static ServiceResource toResourceFromEntity(Service entity) {
        return new ServiceResource(
                entity.getId(),
                entity.getName().name(),
                entity.getDuration().duration(),
                entity.getPrice().price(),
                entity.getProviderId().providerId()
        );
    }
}
