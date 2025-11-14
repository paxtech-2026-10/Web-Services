package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateDiscountCommand;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.UpdateDiscountResource;

public class UpdateDiscountCommandFromResourceAssembler {
    public static UpdateDiscountCommand toCommandFromResource(Long id, UpdateDiscountResource resource) {
        return new UpdateDiscountCommand(
                id,
                resource.title(),
                resource.subtitle(),
                resource.content(),
                resource.discountType(),
                resource.discountValue()
        );
    }
}

