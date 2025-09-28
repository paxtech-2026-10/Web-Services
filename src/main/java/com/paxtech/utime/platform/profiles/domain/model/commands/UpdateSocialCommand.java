// UpdateSocialCommand.java
package com.paxtech.utime.platform.profiles.domain.model.commands;

public record UpdateSocialCommand(Long id, String socialUrl, String socialIcon) { }
