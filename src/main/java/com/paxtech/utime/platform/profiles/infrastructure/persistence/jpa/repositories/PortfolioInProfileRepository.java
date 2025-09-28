package com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.PortfolioInProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioInProfileRepository extends JpaRepository<PortfolioInProfile, Long> {
    List<PortfolioInProfile> findAllByProviderProfileId(Long providerProfileId);
}
