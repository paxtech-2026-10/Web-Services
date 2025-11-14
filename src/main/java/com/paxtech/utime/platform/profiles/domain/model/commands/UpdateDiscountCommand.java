package com.paxtech.utime.platform.profiles.domain.model.commands;

import com.paxtech.utime.platform.profiles.domain.model.valueobjects.DiscountType;

import java.math.BigDecimal;

public record UpdateDiscountCommand(
        Long id,
        String title,
        String subtitle,
        String content,
        DiscountType discountType,
        BigDecimal discountValue
) {
    public UpdateDiscountCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Discount ID cannot be null or less than 1");
        }
        if (title != null && title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (content != null && content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        if (discountValue != null && discountValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("DiscountValue must be greater than zero");
        }
        // Nota: La validación de porcentaje máximo se hace en el aggregate
        // porque necesitamos el discountType actual si solo se actualiza el valor
    }
}

