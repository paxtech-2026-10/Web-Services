package com.paxtech.utime.platform.services.domain.model.commands;

public record CreateServiceCommand(String name, Integer duration, Long price, boolean status, Long providerId) {
}
