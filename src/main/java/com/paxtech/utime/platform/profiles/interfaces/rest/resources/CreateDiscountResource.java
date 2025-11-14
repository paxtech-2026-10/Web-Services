package com.paxtech.utime.platform.profiles.interfaces.rest.resources;

import com.paxtech.utime.platform.profiles.domain.model.valueobjects.DiscountType;

import java.math.BigDecimal;

public record CreateDiscountResource(
        String title,
        String subtitle,
        String content,
        DiscountType discountType,
        BigDecimal discountValue,
        Long providerProfileId
) {}

