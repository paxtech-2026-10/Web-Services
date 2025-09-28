package com.paxtech.utime.platform.reviews.application.internal.queryservices;

import com.paxtech.utime.platform.reviews.domain.model.aggregates.Review;
import com.paxtech.utime.platform.reviews.domain.model.queries.GetAllReviewsQuery;
import com.paxtech.utime.platform.reviews.domain.model.queries.GetReviewByIdQuery;
import com.paxtech.utime.platform.reviews.domain.model.queries.GetReviewBySalonIdAndClientIdQuery;
import com.paxtech.utime.platform.reviews.domain.services.ReviewQueryService;
import com.paxtech.utime.platform.reviews.infrastructure.persistence.jpa.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewQueryServiceImpl implements ReviewQueryService {
    private final ReviewRepository reviewRepository;

    public ReviewQueryServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Optional<Review> handle(GetReviewBySalonIdAndClientIdQuery query) {
        return reviewRepository.findByProviderIdAndClientId(query.salonId(), query.clientId());
    }

    @Override
    public Optional<Review> handle(GetReviewByIdQuery query) {
        return reviewRepository.findById(query.id());
    }

    @Override
    public List<Review> handle(GetAllReviewsQuery query){
        return reviewRepository.findAll();
    }
}
