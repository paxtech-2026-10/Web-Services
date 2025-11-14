package com.paxtech.utime.platform.profiles.domain.model.commands;

/**
 * Command to update a Client entity.
 */
public record UpdateClientCommand(Long id, String firstName, String lastName, String profileImageUrl) {
    
    public UpdateClientCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Client ID cannot be null or less than 1");
        }
        if (firstName != null && firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName != null && lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
    }
}







