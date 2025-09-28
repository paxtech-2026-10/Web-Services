package com.paxtech.utime.platform.workers.domain.model.valueobjects;

public record WorkerPhotoUrl(String url) {
    public WorkerPhotoUrl {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Photo URL must not be null or blank");
        }
        // Optional: validar formato de URL si deseas
    }
}