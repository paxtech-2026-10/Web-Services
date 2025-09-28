package com.paxtech.utime.platform.profiles.domain.model.valueobjects;

public record ProfileImage(String url) {
    public ProfileImage(){
        this(null);
    }

    public String getUrl() {
        return url;
    }
}
