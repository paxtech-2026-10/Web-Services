package com.paxtech.utime.platform.workers.domain.services;

import com.paxtech.utime.platform.workers.domain.model.aggregates.Worker;
import com.paxtech.utime.platform.workers.domain.model.queries.GetAllWorkersQuery;
import com.paxtech.utime.platform.workers.domain.model.queries.GetWorkerByIdQuery;
import com.paxtech.utime.platform.workers.domain.model.queries.GetWorkersBySalonIdQuery;

import java.util.List;
import java.util.Optional;

public interface WorkerQueryService {
    List<Worker> handle(GetWorkersBySalonIdQuery query);

    Optional<Worker> handle(GetWorkerByIdQuery query);

    List<Worker> handle(GetAllWorkersQuery query);
}
