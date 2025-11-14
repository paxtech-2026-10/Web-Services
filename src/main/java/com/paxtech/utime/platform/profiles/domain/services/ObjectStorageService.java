package com.paxtech.utime.platform.profiles.domain.services;

import java.io.IOException;

public interface ObjectStorageService {
    String uploadProfileImage(byte[] fileContent, String contentType, Long clientId) throws IOException;
    void deleteProfileImage(String fileUrl) throws IOException;
    void validateImageFile(byte[] fileContent, String contentType, long fileSize);
}
