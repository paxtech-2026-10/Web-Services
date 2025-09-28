package com.paxtech.utime.platform.reviews.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.reviews.domain.model.aggregates.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByProviderIdAndClientId(Long salonId, Long clientId);

}
