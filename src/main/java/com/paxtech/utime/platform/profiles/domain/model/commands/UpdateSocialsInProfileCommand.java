package com.paxtech.utime.platform.profiles.domain.model.commands;

public record UpdateSocialsInProfileCommand(Long id, Long socialId,
                                            Long salonProfileId) {}
