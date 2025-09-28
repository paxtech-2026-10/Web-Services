package com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.SocialInProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialInProfileRepository extends JpaRepository<SocialInProfile, Long> {
    List<SocialInProfile> findAllByProviderProfileId(Long providerProfileId);

}
