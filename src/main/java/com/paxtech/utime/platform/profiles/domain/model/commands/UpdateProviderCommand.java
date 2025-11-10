package com.paxtech.utime.platform.profiles.domain.model.commands;

/**
 * Command to update a Provider entity.
 */
public record UpdateProviderCommand(Long id, String companyName) {
    
    public UpdateProviderCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Provider ID cannot be null or less than 1");
        }
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
    }
}




