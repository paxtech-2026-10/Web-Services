package com.paxtech.utime.platform.profiles.domain.model.commands;

import com.paxtech.utime.platform.profiles.domain.model.valueobjects.DiscountType;

import java.math.BigDecimal;

public record CreateDiscountCommand(
        String title,
        String subtitle,
        String content,
        DiscountType discountType,
        BigDecimal discountValue,
        Long providerProfileId
) {
    public CreateDiscountCommand {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        if (discountType == null) {
            throw new IllegalArgumentException("DiscountType cannot be null");
        }
        if (discountValue == null || discountValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("DiscountValue must be greater than zero");
        }
        if (discountType == DiscountType.PERCENTAGE && discountValue.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Percentage discount cannot exceed 100%");
        }
        if (providerProfileId == null || providerProfileId <= 0) {
            throw new IllegalArgumentException("ProviderProfileId cannot be null or less than 1");
        }
    }
}

