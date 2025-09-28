package com.paxtech.utime.platform.workers.domain.model.valueobjects;

public record WorkerSpecialization(String specialization) {
    public WorkerSpecialization {
        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException("Specialization must not be null or blank");
        }
    }
}