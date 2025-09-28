package com.paxtech.utime.platform.profiles.domain.model.commands;


/**
 * @summary
 * Command to create a Client entity.
 */
public record CreateClientCommand(String firstName, String lastName, Long userId) {

    public CreateClientCommand {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be null or empty");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be null or empty");
        if (userId == null)
            throw new IllegalArgumentException("User ID cannot be null");
    }
}
