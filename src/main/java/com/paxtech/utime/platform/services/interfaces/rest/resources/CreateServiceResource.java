package com.paxtech.utime.platform.services.interfaces.rest.resources;

public record CreateServiceResource(
        String name,
        int duration,
        Long price,
        boolean status,
        Long providerId
) {
}
