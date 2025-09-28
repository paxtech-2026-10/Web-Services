package com.paxtech.utime.platform.services.domain.model.commands;

public record UpdateServiceCommand(Long id, String name, int duration, Long price) {
}
