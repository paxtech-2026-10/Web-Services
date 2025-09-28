package com.paxtech.utime.platform.workers.domain.model.valueobjects;

public record WorkerName(String name) {
    public WorkerName {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
    }
}
