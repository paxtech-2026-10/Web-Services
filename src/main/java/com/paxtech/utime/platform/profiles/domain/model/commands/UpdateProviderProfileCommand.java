package com.paxtech.utime.platform.profiles.domain.model.commands;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public record UpdateProviderProfileCommand(
        Long id, 
        String profileUrl, 
        String coverUrl, 
        String location, 
        String companyName, 
        Map<String, String> socials, 
        List<String> portfolioImages,
        String description,
        LocalTime openTime,
        LocalTime closeTime
) {
    public UpdateProviderProfileCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id cannot be null or less than 1");
        }
        if (profileUrl != null && profileUrl.isEmpty()) {
            throw new IllegalArgumentException("profileUrl cannot be empty");
        }
        if (coverUrl != null && coverUrl.isEmpty()) {
            throw new IllegalArgumentException("coverUrl cannot be empty");
        }
        if (location != null && location.isEmpty()) {
            throw new IllegalArgumentException("location cannot be empty");
        }
    }
}
