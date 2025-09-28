package com.paxtech.utime.platform.services.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.services.domain.model.aggregates.Service;
import com.paxtech.utime.platform.services.domain.model.valueobjects.Name;
import com.paxtech.utime.platform.services.domain.model.valueobjects.ProviderId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByProviderId(ProviderId providerId);

    boolean existsByProviderIdAndName(ProviderId providerId, Name name);

    List<Service> findByNameAndProviderId(Name name, ProviderId providerId);
}
