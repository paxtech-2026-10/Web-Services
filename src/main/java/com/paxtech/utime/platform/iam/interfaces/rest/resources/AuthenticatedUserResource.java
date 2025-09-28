package com.paxtech.utime.platform.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(Long id, String email, String token) {
}