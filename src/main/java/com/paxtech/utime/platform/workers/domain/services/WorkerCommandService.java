package com.paxtech.utime.platform.workers.domain.services;

import com.paxtech.utime.platform.workers.domain.model.aggregates.Worker;
import com.paxtech.utime.platform.workers.domain.model.commands.CreateWorkerCommand;
import com.paxtech.utime.platform.workers.domain.model.commands.DeleteWorkerCommand;
import com.paxtech.utime.platform.workers.domain.model.commands.UpdateWorkerCommand;

import java.util.Optional;

public interface WorkerCommandService {
    Optional<Worker> handle(CreateWorkerCommand command);

    Optional<Worker> handle(UpdateWorkerCommand command);

    void handle(DeleteWorkerCommand command);
}
