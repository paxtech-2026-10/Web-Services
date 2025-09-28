package com.paxtech.utime.platform.profiles.interfaces.rest.resources;


public record ClientResource(
        Long id,
        String firstName,
        String lastName,
        Long userId
) {}
