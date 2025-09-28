package com.paxtech.utime.platform.profiles.interfaces.rest.resources;

public record AccountResource(Long id, String password, boolean isActive, String username) {

}
