package com.paxtech.utime.platform.workers.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record WorkerName(String name) {
    public WorkerName {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
    }
}
