package com.paxtech.utime.platform.reviews.interfaces.rest.resources;

public record CreateReviewResource(Long clientId, Long providerId, Integer rating, String review) {

}
