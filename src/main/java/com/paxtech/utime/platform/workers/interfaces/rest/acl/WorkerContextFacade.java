package com.paxtech.utime.platform.workers.interfaces.rest.acl;

import com.paxtech.utime.platform.workers.domain.model.aggregates.Worker;

import java.util.Optional;

public interface WorkerContextFacade {
    Optional<Worker> fetchWorkerById(Long id);
}
