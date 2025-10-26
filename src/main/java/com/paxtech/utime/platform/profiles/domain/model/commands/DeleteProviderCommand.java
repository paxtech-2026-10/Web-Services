package com.paxtech.utime.platform.profiles.domain.model.commands;

/**
 * Command to delete a Provider entity.
 */
public record DeleteProviderCommand(Long id) {
    
    public DeleteProviderCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Provider ID cannot be null or less than 1");
        }
    }
}

