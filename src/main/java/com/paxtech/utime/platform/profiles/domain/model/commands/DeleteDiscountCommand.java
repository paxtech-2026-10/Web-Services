package com.paxtech.utime.platform.profiles.domain.model.commands;

public record DeleteDiscountCommand(Long id) {
    public DeleteDiscountCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Discount ID cannot be null or less than 1");
        }
    }
}

