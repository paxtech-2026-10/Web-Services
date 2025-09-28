package com.paxtech.utime.platform.profiles.interfaces.rest.resources;

public record CreateProviderResource(String companyName, Long userId) {
    public CreateProviderResource {
        if (companyName == null || companyName.isBlank())
            throw new IllegalArgumentException("Company name cannot be null or empty");
        if (userId == null)
            throw new IllegalArgumentException("User ID cannot be null");
    }
}
