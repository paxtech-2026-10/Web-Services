package com.paxtech.utime.platform.reviews.interfaces.rest.transform;

import com.paxtech.utime.platform.reviews.domain.model.aggregates.Review;
import com.paxtech.utime.platform.reviews.interfaces.rest.resources.ReviewResource;

public class ReviewResourceFromEntityAssembler {
    public static ReviewResource toResourceFromEntity(Review entity){
        return new ReviewResource(entity.getId(), entity.getClientId(), entity.getProviderId(), entity.getRating(), entity.getReview(), entity.isRead());
    }
}
