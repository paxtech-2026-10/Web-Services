package com.paxtech.utime.platform.profiles.domain.model.commands;

public record CreateProviderProfileCommand(String profileUrl, String coverUrl, Long providerId) {
}
