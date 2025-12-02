package com.paxtech.utime.platform.profiles.interfaces.rest.resources;

import java.time.LocalTime;

public record CreateProviderProfileResource(
        String profileUrl, 
        String coverUrl, 
        String location, 
        Long providerId,
        String description,
        LocalTime openTime,
        LocalTime closeTime
) {

}
