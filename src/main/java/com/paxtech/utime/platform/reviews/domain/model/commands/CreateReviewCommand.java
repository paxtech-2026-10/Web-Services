package com.paxtech.utime.platform.reviews.domain.model.commands;

public record CreateReviewCommand(Long clientId, Long providerId, Integer rating, String review, boolean read) {
    public CreateReviewCommand {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }
}
