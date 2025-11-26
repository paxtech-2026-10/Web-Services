package com.paxtech.utime.platform.profiles.domain.model.commands;

/**
 * Command to delete a Client entity.
 */
public record DeleteClientCommand(Long id) {
    
    public DeleteClientCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Client ID cannot be null or less than 1");
        }
    }
}








