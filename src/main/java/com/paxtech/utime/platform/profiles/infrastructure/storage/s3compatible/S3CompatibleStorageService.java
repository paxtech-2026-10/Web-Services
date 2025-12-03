package com.paxtech.utime.platform.profiles.infrastructure.storage.s3compatible;

import com.paxtech.utime.platform.profiles.domain.services.ObjectStorageService;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public abstract class S3CompatibleStorageService implements ObjectStorageService {
    
    protected static final Logger logger = LoggerFactory.getLogger(S3CompatibleStorageService.class);
    
    protected static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    protected static final String[] ALLOWED_CONTENT_TYPES = {
            "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif"
    };

    protected final OkHttpClient httpClient;

    public S3CompatibleStorageService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();
    }

    protected boolean isAllowedContentType(String contentType) {
        for (String allowedContentType : ALLOWED_CONTENT_TYPES) {
            if (allowedContentType.equals(contentType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void validateImageFile(byte[] fileContent, String contentType, long fileSize) {
        if (fileContent == null || fileContent.length == 0) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new IllegalArgumentException(
                    "Tipo de archivo no permitido. Solo se permiten: JPEG, PNG, WEBP, GIF"
            );
        }

        if (fileSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    String.format("El archivo no puede exceder %d MB", MAX_FILE_SIZE / (1024 * 1024))
            );
        }
    }

    protected String generateFileName(Long clientId, String contentType) {
        String extension = getExtensionFromContentType(contentType);
        // Remover el prefijo "profiles/" ya que el bucket ya se llama "profiles"
        return String.format("clients/%d/%s%s", clientId, UUID.randomUUID(), extension);
    }

    protected String generateProviderProfileFileName(Long providerId, String contentType) {
        String extension = getExtensionFromContentType(contentType);
        return String.format("providers/%d/profile/%s%s", providerId, UUID.randomUUID(), extension);
    }

    protected String generateProviderCoverFileName(Long providerId, String contentType) {
        String extension = getExtensionFromContentType(contentType);
        return String.format("providers/%d/cover/%s%s", providerId, UUID.randomUUID(), extension);
    }

    protected String generateWorkerPhotoFileName(Long workerId, String contentType) {
        String extension = getExtensionFromContentType(contentType);
        return String.format("workers/%d/%s%s", workerId, UUID.randomUUID(), extension);
    }

    private String getExtensionFromContentType(String contentType) {
        if (contentType == null) return ".jpg";

        return switch (contentType.toLowerCase()) {
            case "image/jpeg", "image/jpg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> ".jpg";
        };
    }
    protected abstract String buildUploadUrl(String fileName);
    protected abstract String buildPublicUrl(String fileName);
    protected abstract String buildDeleteUrl(String filePath);
    protected abstract String getAuthorizationHeader();
    protected abstract String extractFilePathFromUrl(String fileUrl);
    protected abstract String getApiKeyHeader();
    
    /**
     * Retorna el signer AWS V4 si está disponible, null si no se usa firma AWS
     */
    protected AwsSignatureV4Signer getAwsSigner() {
        return null; // Por defecto no usar firma AWS
    }

    protected void uploadFile(byte[] fileContent, String contentType, String uploadUrl) throws IOException{
        logger.info("=== Iniciando upload de archivo ===");
        logger.info("Upload URL: {}", uploadUrl);
        logger.info("Content-Type: {}", contentType);
        logger.info("File size: {} bytes", fileContent.length);
        
        RequestBody requestBody = RequestBody.create(
                fileContent,
                MediaType.parse(contentType)
        );

        Request.Builder requestBuilder = new Request.Builder()
                .url(uploadUrl)
                .addHeader("Content-Type", contentType);

        // Si hay un signer AWS, usarlo para firmar el request
        AwsSignatureV4Signer signer = getAwsSigner();
        if (signer != null) {
            logger.info("Usando AWS Signature V4 para autenticación");
            // Construir request temporal para firmar
            Request tempRequest = requestBuilder.post(requestBody).build();
            logger.info("Request temporal construido: {}", tempRequest.url());
            try {
                Request signedRequest = signer.signRequest(tempRequest, fileContent);
                logger.info("Enviando request firmado a: {}", signedRequest.url());
                
                try (Response response = httpClient.newCall(signedRequest).execute()) {
                    logger.info("Respuesta recibida - Status: {}, Message: {}", response.code(), response.message());
                    logger.info("Response headers:");
                    response.headers().forEach(pair -> {
                        logger.info("  {} = {}", pair.getFirst(), pair.getSecond());
                    });
                    
                    if (!response.isSuccessful()) {
                        String errorBody = response.body() != null ? response.body().string() : "";
                        logger.error("Error en respuesta: Status={}, Body={}", response.code(), errorBody);
                        throw new IOException(
                                String.format("Error al subir archivo: %s - %s - %s",
                                        response.code(), response.message(), errorBody)
                        );
                    }
                    logger.info("Upload exitoso!");
                }
                return;
            } catch (Exception e) {
                logger.error("Error al firmar o ejecutar request: {}", e.getMessage(), e);
                throw new IOException("Error al firmar request: " + e.getMessage(), e);
            }
        }

        // Si no hay signer, usar método tradicional (API REST nativa)
        logger.info("Usando autenticación con headers (API REST nativa)");
        String authHeader = getAuthorizationHeader();
        if (authHeader != null && !authHeader.isEmpty()) {
            requestBuilder.addHeader("Authorization", authHeader);
            logger.info("Authorization header agregado: {}...", 
                    authHeader.length() > 30 ? authHeader.substring(0, 30) + "..." : authHeader);
        }

        String apiKey = getApiKeyHeader();
        if (apiKey != null && !apiKey.isEmpty()) {
            requestBuilder.addHeader("apikey", apiKey);
            logger.info("apikey header agregado: {}...", 
                    apiKey.length() > 30 ? apiKey.substring(0, 30) + "..." : apiKey);
        }

        Request request = requestBuilder.post(requestBody).build();
        logger.info("Enviando request a: {}", request.url());

        try (Response response = httpClient.newCall(request).execute()) {
            logger.info("Respuesta recibida - Status: {}, Message: {}", response.code(), response.message());
            logger.info("Response headers:");
            response.headers().forEach(pair -> {
                logger.info("  {} = {}", pair.getFirst(), pair.getSecond());
            });
            
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                logger.error("Error en respuesta: Status={}, Body={}", response.code(), errorBody);
                throw new IOException(
                        String.format("Error al subir archivo: %s - %s - %s",
                                response.code(), response.message(), errorBody)
                );
            }
            logger.info("Upload exitoso!");
        }
    }

    @Override
    public String uploadProfileImage(byte[] fileContent, String contentType, Long clientId) throws IOException {
        logger.info("=== uploadProfileImage iniciado ===");
        logger.info("Client ID: {}", clientId);
        logger.info("Content-Type: {}", contentType);
        
        String fileName = generateFileName(clientId, contentType);
        logger.info("Generated file name: {}", fileName);
        
        String uploadUrl = buildUploadUrl(fileName);
        logger.info("Upload URL construida: {}", uploadUrl);
        
        String publicUrl = buildPublicUrl(fileName);
        logger.info("Public URL construida: {}", publicUrl);

        // Subir el archivo
        uploadFile(fileContent, contentType, uploadUrl);

        logger.info("=== uploadProfileImage completado ===");
        return publicUrl;
    }

    @Override
    public void deleteProfileImage(String fileUrl) throws IOException {
        logger.info("=== deleteProfileImage iniciado ===");
        logger.info("File URL: {}", fileUrl);
        
        String filePath = extractFilePathFromUrl(fileUrl);
        logger.info("Extracted file path: {}", filePath);
        
        String deleteUrl = buildDeleteUrl(filePath);
        logger.info("Delete URL: {}", deleteUrl);

        Request.Builder requestBuilder = new Request.Builder()
                .url(deleteUrl);

        // Si hay un signer AWS, usarlo para firmar el request
        AwsSignatureV4Signer signer = getAwsSigner();
        if (signer != null) {
            logger.info("Usando AWS Signature V4 para eliminar archivo");
            Request tempRequest = requestBuilder.delete().build();
            try {
                Request signedRequest = signer.signRequest(tempRequest, null);
                try (Response response = httpClient.newCall(signedRequest).execute()) {
                    logger.info("Respuesta de eliminación - Status: {}", response.code());
                    if (!response.isSuccessful() && response.code() != 404) {
                        throw new IOException(
                                String.format("Error al eliminar archivo: %s - %s",
                                        response.code(), response.message())
                        );
                    }
                    logger.info("Archivo eliminado exitosamente");
                }
                return;
            } catch (Exception e) {
                logger.error("Error al firmar request de eliminación: {}", e.getMessage(), e);
                throw new IOException("Error al firmar request: " + e.getMessage(), e);
            }
        }

        // Si no hay signer, usar método tradicional (API REST nativa)
        logger.info("Usando autenticación con headers para eliminar (API REST nativa)");
        String authHeader = getAuthorizationHeader();
        if (authHeader != null && !authHeader.isEmpty()) {
            requestBuilder.addHeader("Authorization", authHeader);
            logger.info("Authorization header agregado");
        }

        String apiKey = getApiKeyHeader();
        if (apiKey != null && !apiKey.isEmpty()) {
            requestBuilder.addHeader("apikey", apiKey);
            logger.info("apikey header agregado");
        }

        Request request = requestBuilder.delete().build();
        logger.info("Enviando request DELETE a: {}", request.url());

        try (Response response = httpClient.newCall(request).execute()) {
            logger.info("Respuesta de eliminación - Status: {}, Message: {}", response.code(), response.message());
            
            // Leer el body para verificar si es un error de "not found"
            String errorBody = "";
            if (response.body() != null) {
                errorBody = response.body().string();
            }
            
            // Verificar si es un error que debemos ignorar (404 o 400 con "not found")
            boolean isNotFound = response.code() == 404 || 
                                (response.code() == 400 && 
                                 (errorBody.contains("not_found") || 
                                  errorBody.contains("Object not found") ||
                                  errorBody.contains("\"statusCode\":\"404\"")));
            
            if (!response.isSuccessful() && !isNotFound) {
                logger.error("Error al eliminar archivo: Status={}, Body={}", response.code(), errorBody);
                throw new IOException(
                        String.format("Error al eliminar archivo: %s - %s - %s",
                                response.code(), response.message(), errorBody)
                );
            }
            
            if (isNotFound) {
                logger.info("Archivo no encontrado (ya fue eliminado o no existe) - esto es normal");
            } else {
                logger.info("Archivo eliminado exitosamente");
            }
        }
        logger.info("=== deleteProfileImage completado ===");
    }

    @Override
    public String uploadProviderProfileImage(byte[] fileContent, String contentType, Long providerId) throws IOException {
        logger.info("=== uploadProviderProfileImage iniciado ===");
        logger.info("Provider ID: {}", providerId);
        logger.info("Content-Type: {}", contentType);
        
        String fileName = generateProviderProfileFileName(providerId, contentType);
        logger.info("Generated file name: {}", fileName);
        
        String uploadUrl = buildUploadUrl(fileName);
        logger.info("Upload URL construida: {}", uploadUrl);
        
        String publicUrl = buildPublicUrl(fileName);
        logger.info("Public URL construida: {}", publicUrl);

        // Subir el archivo
        uploadFile(fileContent, contentType, uploadUrl);

        logger.info("=== uploadProviderProfileImage completado ===");
        return publicUrl;
    }

    @Override
    public String uploadProviderCoverImage(byte[] fileContent, String contentType, Long providerId) throws IOException {
        logger.info("=== uploadProviderCoverImage iniciado ===");
        logger.info("Provider ID: {}", providerId);
        logger.info("Content-Type: {}", contentType);
        
        String fileName = generateProviderCoverFileName(providerId, contentType);
        logger.info("Generated file name: {}", fileName);
        
        String uploadUrl = buildUploadUrl(fileName);
        logger.info("Upload URL construida: {}", uploadUrl);
        
        String publicUrl = buildPublicUrl(fileName);
        logger.info("Public URL construida: {}", publicUrl);

        // Subir el archivo
        uploadFile(fileContent, contentType, uploadUrl);

        logger.info("=== uploadProviderCoverImage completado ===");
        return publicUrl;
    }

    @Override
    public void deleteProviderImage(String fileUrl) throws IOException {
        logger.info("=== deleteProviderImage iniciado ===");
        logger.info("File URL: {}", fileUrl);
        
        String filePath = extractFilePathFromUrl(fileUrl);
        logger.info("Extracted file path: {}", filePath);
        
        String deleteUrl = buildDeleteUrl(filePath);
        logger.info("Delete URL: {}", deleteUrl);

        Request.Builder requestBuilder = new Request.Builder()
                .url(deleteUrl);

        // Si hay un signer AWS, usarlo para firmar el request
        AwsSignatureV4Signer signer = getAwsSigner();
        if (signer != null) {
            logger.info("Usando AWS Signature V4 para eliminar archivo");
            Request tempRequest = requestBuilder.delete().build();
            try {
                Request signedRequest = signer.signRequest(tempRequest, null);
                try (Response response = httpClient.newCall(signedRequest).execute()) {
                    logger.info("Respuesta de eliminación - Status: {}", response.code());
                    if (!response.isSuccessful() && response.code() != 404) {
                        throw new IOException(
                                String.format("Error al eliminar archivo: %s - %s",
                                        response.code(), response.message())
                        );
                    }
                    logger.info("Archivo eliminado exitosamente");
                }
                return;
            } catch (Exception e) {
                logger.error("Error al firmar request de eliminación: {}", e.getMessage(), e);
                throw new IOException("Error al firmar request: " + e.getMessage(), e);
            }
        }

        // Si no hay signer, usar método tradicional (API REST nativa)
        logger.info("Usando autenticación con headers para eliminar (API REST nativa)");
        String authHeader = getAuthorizationHeader();
        if (authHeader != null && !authHeader.isEmpty()) {
            requestBuilder.addHeader("Authorization", authHeader);
            logger.info("Authorization header agregado");
        }

        String apiKey = getApiKeyHeader();
        if (apiKey != null && !apiKey.isEmpty()) {
            requestBuilder.addHeader("apikey", apiKey);
            logger.info("apikey header agregado");
        }

        Request request = requestBuilder.delete().build();
        logger.info("Enviando request DELETE a: {}", request.url());

        try (Response response = httpClient.newCall(request).execute()) {
            logger.info("Respuesta de eliminación - Status: {}, Message: {}", response.code(), response.message());
            
            String errorBody = "";
            if (response.body() != null) {
                errorBody = response.body().string();
            }
            
            boolean isNotFound = response.code() == 404 || 
                                (response.code() == 400 && 
                                 (errorBody.contains("not_found") || 
                                  errorBody.contains("Object not found") ||
                                  errorBody.contains("\"statusCode\":\"404\"")));
            
            if (!response.isSuccessful() && !isNotFound) {
                logger.error("Error al eliminar archivo: Status={}, Body={}", response.code(), errorBody);
                throw new IOException(
                        String.format("Error al eliminar archivo: %s - %s - %s",
                                response.code(), response.message(), errorBody)
                );
            }
            
            if (isNotFound) {
                logger.info("Archivo no encontrado (ya fue eliminado o no existe) - esto es normal");
            } else {
                logger.info("Archivo eliminado exitosamente");
            }
        }
        logger.info("=== deleteProviderImage completado ===");
    }

    @Override
    public String uploadWorkerPhotoImage(byte[] fileContent, String contentType, Long workerId) throws IOException {
        logger.info("=== uploadWorkerPhotoImage iniciado ===");
        logger.info("Worker ID: {}", workerId);
        logger.info("Content-Type: {}", contentType);
        
        String fileName = generateWorkerPhotoFileName(workerId, contentType);
        logger.info("Generated file name: {}", fileName);
        
        String uploadUrl = buildUploadUrl(fileName);
        logger.info("Upload URL construida: {}", uploadUrl);
        
        String publicUrl = buildPublicUrl(fileName);
        logger.info("Public URL construida: {}", publicUrl);

        // Subir el archivo
        uploadFile(fileContent, contentType, uploadUrl);

        logger.info("=== uploadWorkerPhotoImage completado ===");
        return publicUrl;
    }

    @Override
    public void deleteWorkerImage(String fileUrl) throws IOException {
        logger.info("=== deleteWorkerImage iniciado ===");
        logger.info("File URL: {}", fileUrl);
        
        String filePath = extractFilePathFromUrl(fileUrl);
        logger.info("Extracted file path: {}", filePath);
        
        String deleteUrl = buildDeleteUrl(filePath);
        logger.info("Delete URL: {}", deleteUrl);

        Request.Builder requestBuilder = new Request.Builder()
                .url(deleteUrl);

        // Si hay un signer AWS, usarlo para firmar el request
        AwsSignatureV4Signer signer = getAwsSigner();
        if (signer != null) {
            logger.info("Usando AWS Signature V4 para eliminar archivo");
            Request tempRequest = requestBuilder.delete().build();
            try {
                Request signedRequest = signer.signRequest(tempRequest, null);
                try (Response response = httpClient.newCall(signedRequest).execute()) {
                    logger.info("Respuesta de eliminación - Status: {}", response.code());
                    if (!response.isSuccessful() && response.code() != 404) {
                        throw new IOException(
                                String.format("Error al eliminar archivo: %s - %s",
                                        response.code(), response.message())
                        );
                    }
                    logger.info("Archivo eliminado exitosamente");
                }
                return;
            } catch (Exception e) {
                logger.error("Error al firmar request de eliminación: {}", e.getMessage(), e);
                throw new IOException("Error al firmar request: " + e.getMessage(), e);
            }
        }

        // Si no hay signer, usar método tradicional (API REST nativa)
        logger.info("Usando autenticación con headers para eliminar (API REST nativa)");
        String authHeader = getAuthorizationHeader();
        if (authHeader != null && !authHeader.isEmpty()) {
            requestBuilder.addHeader("Authorization", authHeader);
            logger.info("Authorization header agregado");
        }

        String apiKey = getApiKeyHeader();
        if (apiKey != null && !apiKey.isEmpty()) {
            requestBuilder.addHeader("apikey", apiKey);
            logger.info("apikey header agregado");
        }

        Request request = requestBuilder.delete().build();
        logger.info("Enviando request DELETE a: {}", request.url());

        try (Response response = httpClient.newCall(request).execute()) {
            logger.info("Respuesta de eliminación - Status: {}, Message: {}", response.code(), response.message());
            
            String errorBody = "";
            if (response.body() != null) {
                errorBody = response.body().string();
            }
            
            boolean isNotFound = response.code() == 404 || 
                                (response.code() == 400 && 
                                 (errorBody.contains("not_found") || 
                                  errorBody.contains("Object not found") ||
                                  errorBody.contains("\"statusCode\":\"404\"")));
            
            if (!response.isSuccessful() && !isNotFound) {
                logger.error("Error al eliminar archivo: Status={}, Body={}", response.code(), errorBody);
                throw new IOException(
                        String.format("Error al eliminar archivo: %s - %s - %s",
                                response.code(), response.message(), errorBody)
                );
            }
            
            if (isNotFound) {
                logger.info("Archivo no encontrado (ya fue eliminado o no existe) - esto es normal");
            } else {
                logger.info("Archivo eliminado exitosamente");
            }
        }
        logger.info("=== deleteWorkerImage completado ===");
    }
}
