package com.paxtech.utime.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record CoverImage(String CoverUrl) {
    public CoverImage(){
        this(null);
    }

    public String getUrl() {
        return CoverUrl;
    }
}
