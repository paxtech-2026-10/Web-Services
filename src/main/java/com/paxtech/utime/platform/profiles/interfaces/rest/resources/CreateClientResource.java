package com.paxtech.utime.platform.profiles.interfaces.rest.resources;


public record CreateClientResource(
        String firstName,
        String lastName,
        Long userId
) {
    public CreateClientResource {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be null or empty");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be null or empty");
        if (userId == null)
            throw new IllegalArgumentException("User ID cannot be null");
    }
}
