package com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Social;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocialRepository extends JpaRepository<Social, Long> {
    Optional<Social> findBySocialIcon(String socialIcon);
    Optional<Social> findBySocialUrl(String socialUrl);
    List<Social> findAllByIdIn(List<Long> ids);

}
