package com.paxtech.utime.platform.workers.domain.model.commands;

public record CreateWorkerCommand(
        String name,
        String specialization,
        String photoUrl,
        Long salonId
) {}