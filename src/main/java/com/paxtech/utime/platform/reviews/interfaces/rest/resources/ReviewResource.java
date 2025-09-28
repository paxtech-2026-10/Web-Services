package com.paxtech.utime.platform.reviews.interfaces.rest.resources;

public record ReviewResource(Long id, Long clientId, Long providerId, Integer rating, String review, boolean read) {
}
