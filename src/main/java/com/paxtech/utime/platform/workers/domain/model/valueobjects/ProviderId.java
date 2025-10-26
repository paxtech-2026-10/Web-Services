package com.paxtech.utime.platform.workers.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProviderId(Long providerId) {
    public ProviderId {
        if (providerId == null || providerId <= 0) {
            throw new IllegalArgumentException("Salon ID must be a positive number");
        }
    }
}
