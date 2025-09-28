package com.paxtech.utime.platform.workers.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.workers.domain.model.aggregates.Worker;
import com.paxtech.utime.platform.workers.domain.model.valueobjects.ProviderId;
import com.paxtech.utime.platform.workers.domain.model.valueobjects.WorkerName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    List<Worker> findByProviderId(ProviderId providerId);

    List<Worker> findAllByProviderId(ProviderId providerId);

    boolean existsByNameAndProviderId(WorkerName name, ProviderId providerId);

    boolean existsByIdIsNot(Long id);

    Long id(Long id);
}
