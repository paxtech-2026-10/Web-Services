package com.paxtech.utime.platform.profiles.interfaces.rest.resources;

/**
 * Resource for updating a Client
 */
public record UpdateClientResource(
    String firstName,
    String lastName,
    String profileImageUrl
) {
    public UpdateClientResource {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
    }
}







