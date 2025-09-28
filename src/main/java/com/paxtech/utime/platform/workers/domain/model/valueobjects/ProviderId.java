package com.paxtech.utime.platform.workers.domain.model.valueobjects;

public record ProviderId(Long providerId) {
    public ProviderId {
        if (providerId == null || providerId <= 0) {
            throw new IllegalArgumentException("Salon ID must be a positive number");
        }
    }
}
