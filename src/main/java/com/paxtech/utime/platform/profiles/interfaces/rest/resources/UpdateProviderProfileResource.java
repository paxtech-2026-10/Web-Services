package com.paxtech.utime.platform.profiles.interfaces.rest.resources;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public record UpdateProviderProfileResource(
    String profileImageUrl,
    String coverImageUrl,
    String location,
    String companyName,
    Map<String, String> socials,
    List<String> portfolioImages,
    String description,
    LocalTime openTime,
    LocalTime closeTime
) {}
