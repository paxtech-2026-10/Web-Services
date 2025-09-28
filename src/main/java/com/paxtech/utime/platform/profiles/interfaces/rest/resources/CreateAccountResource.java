package com.paxtech.utime.platform.profiles.interfaces.rest.resources;

public record CreateAccountResource (String password, boolean isActive, String username) {
    public CreateAccountResource {
        if(password == null || password.isBlank()){
            throw new IllegalArgumentException("password is blank");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username is blank");
        }
    }
}
