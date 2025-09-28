package com.paxtech.utime.platform.profiles.interfaces.rest.resources;


import java.util.List;
import java.util.Map;

public record ProfileResource(
        Long id,
        Long providerId,
        String companyName,
        String location,
        String email,
        String profileImageUrl,
        String coverImageUrl,
        Map<String, String> socials,
        List<PortfolioImageResource> portfolioImages
) {}
