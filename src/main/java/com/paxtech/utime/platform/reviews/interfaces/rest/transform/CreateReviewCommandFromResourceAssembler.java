package com.paxtech.utime.platform.reviews.interfaces.rest.transform;

import com.paxtech.utime.platform.reviews.domain.model.commands.CreateReviewCommand;
import com.paxtech.utime.platform.reviews.interfaces.rest.resources.CreateReviewResource;

public class CreateReviewCommandFromResourceAssembler {
    public static CreateReviewCommand toCommandFromResource(CreateReviewResource resource){
        return new CreateReviewCommand(resource.clientId(), resource.providerId(), resource.rating(), resource.review(), false);
    }
}
