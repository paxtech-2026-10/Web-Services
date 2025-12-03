package com.paxtech.utime.platform.profiles.domain.services;

import java.io.IOException;

public interface ObjectStorageService {
    // Client methods
    String uploadProfileImage(byte[] fileContent, String contentType, Long clientId) throws IOException;
    void deleteProfileImage(String fileUrl) throws IOException;
    
    // Provider methods
    String uploadProviderProfileImage(byte[] fileContent, String contentType, Long providerId) throws IOException;
    String uploadProviderCoverImage(byte[] fileContent, String contentType, Long providerId) throws IOException;
    void deleteProviderImage(String fileUrl) throws IOException;
    
    // Worker methods
    String uploadWorkerPhotoImage(byte[] fileContent, String contentType, Long workerId) throws IOException;
    void deleteWorkerImage(String fileUrl) throws IOException;
    
    // Shared validation
    void validateImageFile(byte[] fileContent, String contentType, long fileSize);
}
