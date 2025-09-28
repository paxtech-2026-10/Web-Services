package com.paxtech.utime.platform.profiles.domain.model.valueobjects;

public record CoverImage(String CoverUrl) {
    public CoverImage(){
        this(null);
    }

    public String getUrl() {
        return CoverUrl;
    }
}
