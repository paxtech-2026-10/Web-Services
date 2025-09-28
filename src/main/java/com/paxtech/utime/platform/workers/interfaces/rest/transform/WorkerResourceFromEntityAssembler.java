package com.paxtech.utime.platform.workers.interfaces.rest.transform;

import com.paxtech.utime.platform.workers.domain.model.aggregates.Worker;
import com.paxtech.utime.platform.workers.interfaces.rest.resources.WorkerResource;

public class WorkerResourceFromEntityAssembler {
    public static WorkerResource toResourceFromEntity(Worker worker) {
        return new WorkerResource(
                worker.getId(),
                worker.getName(),
                worker.getSpecialization(),
                worker.getPhotoUrl(),
                worker.getSalonId()
        );
    }
}