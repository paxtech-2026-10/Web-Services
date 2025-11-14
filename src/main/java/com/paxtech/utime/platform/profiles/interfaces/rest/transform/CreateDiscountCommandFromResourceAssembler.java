package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateDiscountCommand;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.CreateDiscountResource;

public class CreateDiscountCommandFromResourceAssembler {
    public static CreateDiscountCommand toCommandFromResource(CreateDiscountResource resource) {
        return new CreateDiscountCommand(
                resource.title(),
                resource.subtitle(),
                resource.content(),
                resource.discountType(),
                resource.discountValue(),
                resource.providerProfileId()
        );
    }
}

