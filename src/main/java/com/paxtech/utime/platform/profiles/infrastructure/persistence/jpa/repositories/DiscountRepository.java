package com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findAllByProviderProfileId(Long providerProfileId);
}

