package com.paxtech.utime.platform.profiles.interfaces.rest.resources;

import java.util.List;
import java.util.Map;

public record CreateFullProfileResource(
        Long providerId,
        String companyName,
        String location,
        String email,
        String profileImageUrl,
        String coverImageUrl,
        Map<String, String> socials,      // ej: {"instagram": "url"}
        List<String> portfolioImages      // ej: ["https://img1", "https://img2"]
) {}
