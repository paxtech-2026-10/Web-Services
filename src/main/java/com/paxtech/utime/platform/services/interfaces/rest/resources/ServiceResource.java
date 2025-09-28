package com.paxtech.utime.platform.services.interfaces.rest.resources;

public record ServiceResource(
        Long id,
        String name,
        Integer duration,
        Long price,
        Long providerId
) {
}
