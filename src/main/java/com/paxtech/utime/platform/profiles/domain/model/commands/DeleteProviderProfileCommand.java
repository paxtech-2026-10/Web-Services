package com.paxtech.utime.platform.profiles.domain.model.commands;

public record DeleteProviderProfileCommand(Long id) {
    public DeleteProviderProfileCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id cannot be null or less than 1");
        }
    }
}
