package com.paxtech.utime.platform.workers.application.acl;

import com.paxtech.utime.platform.workers.domain.model.aggregates.Worker;
import com.paxtech.utime.platform.workers.infrastructure.persistence.jpa.repositories.WorkerRepository;
import com.paxtech.utime.platform.workers.interfaces.rest.acl.WorkerContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkerContextFacadeImpl implements WorkerContextFacade {
    private final WorkerRepository workerRepository;

    public WorkerContextFacadeImpl(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }
    @Override
    public Optional<Worker> fetchWorkerById(Long workerId) {
        return workerRepository.findById(workerId);
    }
}
