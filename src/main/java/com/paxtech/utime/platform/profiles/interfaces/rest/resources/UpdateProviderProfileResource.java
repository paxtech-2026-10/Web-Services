package com.paxtech.utime.platform.profiles.interfaces.rest.resources;

import java.util.List;
import java.util.Map;

public record UpdateProviderProfileResource(
    String profileImageUrl,
    String coverImageUrl
) {}
