package com.paxtech.utime.platform.profiles.domain.model.commands;

/**
 * @summary
 * Command to create a Provider entity.
 */
public record CreateProviderCommand(String companyName, Long userId) {

    public CreateProviderCommand {
        if (companyName == null || companyName.isBlank())
            throw new IllegalArgumentException("Company name cannot be null or empty");
        if (userId == null)
            throw new IllegalArgumentException("User ID cannot be null");
    }
}
