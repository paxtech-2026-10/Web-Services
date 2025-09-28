package com.paxtech.utime.platform.reservations.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record TimeSlotType(String type) {
    public TimeSlotType {
        if (type == null || type.isBlank())
            throw new IllegalArgumentException("TimeSlot type must not be blank");
        if (type.length() > 10)
            throw new IllegalArgumentException("TimeSlot type exceeds 10 characters");
    }
}
