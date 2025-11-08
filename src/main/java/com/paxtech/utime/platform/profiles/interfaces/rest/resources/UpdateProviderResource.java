package com.paxtech.utime.platform.profiles.interfaces.rest.resources;

/**
 * Resource for updating a Provider
 */
public record UpdateProviderResource(
    String companyName
) {
    public UpdateProviderResource {
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
    }
}



