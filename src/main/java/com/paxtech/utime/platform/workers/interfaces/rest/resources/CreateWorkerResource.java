package com.paxtech.utime.platform.workers.interfaces.rest.resources;

public record CreateWorkerResource(String name, String specialization, String photoUrl, Long providerId) {
    public CreateWorkerResource {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException("Specialization must not be null or blank");
        }
        if (photoUrl == null || photoUrl.isBlank()) {
            throw new IllegalArgumentException("Photo URL must not be null or blank");
        }
        if (providerId == null || providerId <= 0) {
            throw new IllegalArgumentException("Salon ID must be a positive number");
        }
    }
}
