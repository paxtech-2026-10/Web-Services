package com.paxtech.utime.platform.workers.domain.model.commands;

public record DeleteWorkerCommand(Long id) {
    public DeleteWorkerCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Worker ID must be a positive number");
        }
    }
}
