package com.paxtech.utime.platform.profiles.domain.model.commands;

import java.time.LocalTime;

public record CreateProviderProfileCommand(
        String profileUrl, 
        String coverUrl, 
        String location, 
        Long providerId,
        String description,
        LocalTime openTime,
        LocalTime closeTime
) {
}
