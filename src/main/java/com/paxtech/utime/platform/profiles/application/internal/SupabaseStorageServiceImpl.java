package com.paxtech.utime.platform.profiles.application.internal;

import com.paxtech.utime.platform.profiles.infrastructure.storage.s3compatible.S3CompatibleStorageService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementación de Supabase Storage usando la API REST nativa.
 * Esta implementación usa Bearer token con service_role_key, que es más simple
 * que usar la API S3 compatible con AWS Signature V4.
 */
@Service
public class SupabaseStorageServiceImpl extends S3CompatibleStorageService {
    
    private static final Logger logger = LoggerFactory.getLogger(SupabaseStorageServiceImpl.class);
    
    @Value("${storage.supabase.url}")
    private String supabaseUrl;
    
    @Value("${storage.supabase.bucket}")
    private String bucketName;
    
    @Value("${storage.supabase.service-role-key}")
    private String serviceRoleKey;
    
    // Log de configuración después de que Spring inyecte los valores
    @PostConstruct
    public void logConfiguration() {
        logger.info("=== SupabaseStorageServiceImpl Configuración (REST API) ===");
        logger.info("Supabase URL: {}", supabaseUrl);
        logger.info("Bucket: {}", bucketName);
        logger.info("Service Role Key: {}...{}", 
                serviceRoleKey != null && serviceRoleKey.length() > 8 ? serviceRoleKey.substring(0, 8) + "..." : "null",
                serviceRoleKey != null && serviceRoleKey.length() > 8 ? " (length: " + serviceRoleKey.length() + ")" : "");
        logger.info("=== Fin Configuración ===");
    }
    
    @Override
    protected com.paxtech.utime.platform.profiles.infrastructure.storage.s3compatible.AwsSignatureV4Signer getAwsSigner() {
        // No usar AWS Signature V4, usar API REST nativa con Bearer token
        return null;
    }

    @Override
    protected String buildUploadUrl(String fileName) {
        // API REST nativa de Supabase: /storage/v1/object/{bucket}/{path}
        // fileName es "clients/7/uuid.png"
        // Ejemplo: https://xxx.supabase.co/storage/v1/object/profiles/clients/7/uuid.png
        String url = String.format("%s/storage/v1/object/%s/%s", supabaseUrl, bucketName, fileName);
        logger.debug("buildUploadUrl - fileName: {}, URL construida: {}", fileName, url);
        return url;
    }

    @Override
    protected String buildPublicUrl(String fileName) {
        // URL pública de Supabase Storage
        // IMPORTANTE: El bucket debe estar configurado como "Public" en Supabase Dashboard
        // Si el bucket es privado, esta URL no funcionará y necesitarás usar signed URLs
        // Ejemplo: https://xxx.supabase.co/storage/v1/object/public/profiles/clients/7/uuid.png
        String publicUrl = String.format("%s/storage/v1/object/public/%s/%s", supabaseUrl, bucketName, fileName);
        logger.debug("buildPublicUrl - fileName: {}, URL pública construida: {}", fileName, publicUrl);
        return publicUrl;
    }

    @Override
    protected String buildDeleteUrl(String filePath) {
        // API REST nativa para eliminar: /storage/v1/object/{bucket}/{path}
        return String.format("%s/storage/v1/object/%s/%s", supabaseUrl, bucketName, filePath);
    }

    @Override
    protected String getAuthorizationHeader() {
        // Para operaciones del servidor, podemos usar solo el apikey header
        // Si el service_role_key es un JWT válido, podemos usarlo aquí
        // Pero primero intentemos sin Authorization header, solo con apikey
        // Si el service_role_key parece ser un JWT (tiene puntos), usarlo como Bearer token
        if (serviceRoleKey != null && serviceRoleKey.contains(".") && serviceRoleKey.split("\\.").length == 3) {
            // Es un JWT válido, usarlo como Bearer token
            return "Bearer " + serviceRoleKey;
        }
        // Si no es un JWT, no usar Authorization header, solo apikey
        return null;
    }

    @Override
    protected String getApiKeyHeader() {
        // API REST nativa requiere el apikey header con el service_role_key
        // Este es el header principal para autenticación del servidor
        return serviceRoleKey;
    }

    @Override
    protected String extractFilePathFromUrl(String url) {
        // Extraer el path del archivo de la URL pública
        // Ejemplo: https://xxx.supabase.co/storage/v1/object/public/profiles/clients/1/uuid.jpg
        // Retorna: clients/1/uuid.jpg (sin el prefijo "profiles/" ya que está en el bucket)
        String publicPath = "/storage/v1/object/public/" + bucketName + "/";
        int index = url.indexOf(publicPath);
        if (index != -1) {
            return url.substring(index + publicPath.length());
        }
        
        // Si no se puede extraer, retornar la URL completa (fallback)
        return url;
    }
}
