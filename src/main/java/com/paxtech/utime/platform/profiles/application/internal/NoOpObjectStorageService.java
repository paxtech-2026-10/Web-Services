package com.paxtech.utime.platform.profiles.application.internal;

import com.paxtech.utime.platform.profiles.domain.services.ObjectStorageService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Profile("test")
public class NoOpObjectStorageService implements ObjectStorageService {
    @Override
    public String uploadProfileImage(byte[] fileContent, String contentType, Long clientId) throws IOException {
        return "test://clients/" + clientId + "/profile-image";
    }

    @Override
    public void deleteProfileImage(String fileUrl) throws IOException {
    }

    @Override
    public String uploadProviderProfileImage(byte[] fileContent, String contentType, Long providerId) throws IOException {
        return "test://providers/" + providerId + "/profile-image";
    }

    @Override
    public String uploadProviderCoverImage(byte[] fileContent, String contentType, Long providerId) throws IOException {
        return "test://providers/" + providerId + "/cover-image";
    }

    @Override
    public void deleteProviderImage(String fileUrl) throws IOException {
    }

    @Override
    public String uploadWorkerPhotoImage(byte[] fileContent, String contentType, Long workerId) throws IOException {
        return "test://workers/" + workerId + "/photo-image";
    }

    @Override
    public void deleteWorkerImage(String fileUrl) throws IOException {
    }

    @Override
    public void validateImageFile(byte[] fileContent, String contentType, long fileSize) {
    }
}
