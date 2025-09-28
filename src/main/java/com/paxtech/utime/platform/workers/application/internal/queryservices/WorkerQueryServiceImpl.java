package com.paxtech.utime.platform.workers.application.internal.queryservices;

import com.paxtech.utime.platform.workers.domain.model.aggregates.Worker;
import com.paxtech.utime.platform.workers.domain.model.queries.GetAllWorkersQuery;
import com.paxtech.utime.platform.workers.domain.model.queries.GetWorkerByIdQuery;
import com.paxtech.utime.platform.workers.domain.model.queries.GetWorkersBySalonIdQuery;
import com.paxtech.utime.platform.workers.domain.services.WorkerQueryService;
import com.paxtech.utime.platform.workers.infrastructure.persistence.jpa.repositories.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerQueryServiceImpl implements WorkerQueryService {
    private final WorkerRepository workerRepository;

    public WorkerQueryServiceImpl(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    @Override
    public List<Worker> handle(GetWorkersBySalonIdQuery query){
        return workerRepository.findAllByProviderId(query.providerId());
    }

    @Override
    public Optional<Worker> handle(GetWorkerByIdQuery query){
        return workerRepository.findById(query.Id());
    }

    @Override
    public List<Worker> handle(GetAllWorkersQuery query){
        return workerRepository.findAll();
    }
}
