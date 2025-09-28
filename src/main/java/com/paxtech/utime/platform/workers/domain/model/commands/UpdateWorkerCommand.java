package com.paxtech.utime.platform.workers.domain.model.commands;

public record UpdateWorkerCommand(Long id,String name, String specialization, String photoUrl, Long providerId) {
}
