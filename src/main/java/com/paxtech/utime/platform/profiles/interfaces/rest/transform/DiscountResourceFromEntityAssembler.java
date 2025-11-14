package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Discount;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.DiscountResource;

public class DiscountResourceFromEntityAssembler {
    public static DiscountResource toResourceFromEntity(Discount entity) {
        return new DiscountResource(
                entity.getId(),
                entity.getTitle(),
                entity.getSubtitle(),
                entity.getContent(),
                entity.getDiscountType(),
                entity.getDiscountValue(),
                entity.getProviderProfile().getId()
        );
    }
}

