// CreateSocialsInProfilesCommand.java
package com.paxtech.utime.platform.profiles.domain.model.commands;

public record CreateSocialsInProfilesCommand(Long socialId, Long providerProfileId, String socialUrl) { }
