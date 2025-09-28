package com.paxtech.utime.platform.reviews.domain.services;

import com.paxtech.utime.platform.reviews.domain.model.aggregates.Review;
import com.paxtech.utime.platform.reviews.domain.model.queries.GetAllReviewsQuery;
import com.paxtech.utime.platform.reviews.domain.model.queries.GetReviewByIdQuery;
import com.paxtech.utime.platform.reviews.domain.model.queries.GetReviewBySalonIdAndClientIdQuery;

import java.util.List;
import java.util.Optional;

public interface ReviewQueryService {
    Optional<Review> handle(GetReviewByIdQuery query);

    List<Review> handle(GetAllReviewsQuery query);

    Optional<Review> handle(GetReviewBySalonIdAndClientIdQuery query);
}
