package com.paxtech.utime.platform.profiles.infrastructure.storage.s3compatible;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.http.HttpMethodName;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class para generar AWS Signature V4 para requests S3-compatibles
 */
public class AwsSignatureV4Signer {
    
    private static final Logger logger = LoggerFactory.getLogger(AwsSignatureV4Signer.class);
    
    private final String accessKeyId;
    private final String secretAccessKey;
    private final String region;
    private final AWS4Signer signer;
    
    public AwsSignatureV4Signer(String accessKeyId, String secretAccessKey, String region) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.region = region;
        this.signer = new AWS4Signer();
        this.signer.setRegionName(region);
        this.signer.setServiceName("s3");
    }
    
    /**
     * Firma un request OkHttp usando AWS Signature V4
     */
    public Request signRequest(Request originalRequest, byte[] body) throws URISyntaxException {
        URI uri = originalRequest.url().uri();
        
        logger.info("=== AWS Signature V4 Signing ===");
        logger.info("Original URL: {}", originalRequest.url());
        logger.info("URI Scheme: {}, Authority: {}, Path: {}", uri.getScheme(), uri.getAuthority(), uri.getPath());
        logger.info("Access Key ID: {}...{}", accessKeyId.substring(0, Math.min(8, accessKeyId.length())), 
                accessKeyId.length() > 8 ? "..." : "");
        logger.info("Region: {}", region);
        logger.info("Body size: {} bytes", body != null ? body.length : 0);
        
        // Convertir el método OkHttp a HttpMethodName de AWS
        HttpMethodName method = HttpMethodName.valueOf(originalRequest.method());
        logger.info("HTTP Method: {}", method);
        
        // Extraer headers (preservar Content-Type y otros headers importantes)
        Map<String, String> headers = new HashMap<>();
        originalRequest.headers().forEach(pair -> {
            String name = pair.getFirst().toLowerCase();
            // No incluir headers que AWS SDK agregará automáticamente
            if (!name.equals("authorization") && 
                !name.equals("x-amz-date") && 
                !name.equals("host") &&
                !name.equals("x-amz-content-sha256")) {
                headers.put(pair.getFirst(), pair.getSecond());
                logger.debug("Header preservado: {} = {}", pair.getFirst(), pair.getSecond());
            }
        });
        logger.info("Headers a incluir en firma: {}", headers);
        
        // Crear credenciales
        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        
        // Crear request para AWS SDK
        com.amazonaws.DefaultRequest<?> awsRequest = new com.amazonaws.DefaultRequest<>("s3");
        
        // Configurar el endpoint base (sin el path del objeto)
        URI baseUri = new URI(uri.getScheme(), uri.getAuthority(), null, null, null);
        awsRequest.setEndpoint(baseUri);
        logger.info("Endpoint base: {}", baseUri);
        
        // Configurar el path del recurso (bucket/key)
        // Para Supabase S3 API: /storage/v1/s3/bucket/key
        String fullPath = uri.getPath();
        logger.info("Full Path del URI: {}", fullPath);
        
        // Extraer el resource path para la firma
        // Probamos diferentes formatos según lo que Supabase pueda requerir
        String resourcePath = extractS3ResourcePath(fullPath);
        awsRequest.setResourcePath(resourcePath);
        logger.info("Resource Path configurado para firma: {}", resourcePath);
        logger.info("Resource Path length: {}", resourcePath.length());
        
        awsRequest.setHttpMethod(method);
        awsRequest.setHeaders(headers);
        
        if (body != null && body.length > 0) {
            awsRequest.setContent(new java.io.ByteArrayInputStream(body));
        }
        
        // Firmar el request
        logger.info("Firmando request...");
        signer.sign(awsRequest, credentials);
        
        logger.info("Headers después de firmar:");
        awsRequest.getHeaders().forEach((key, value) -> {
            if (key.equalsIgnoreCase("Authorization")) {
                logger.info("  {} = {}...", key, value.length() > 50 ? value.substring(0, 50) + "..." : value);
            } else {
                logger.info("  {} = {}", key, value);
            }
        });
        
        // Construir el nuevo request OkHttp con los headers firmados
        Request.Builder newRequestBuilder = originalRequest.newBuilder();
        
        // Limpiar headers anteriores que puedan interferir
        originalRequest.headers().forEach(pair -> {
            String name = pair.getFirst().toLowerCase();
            if (name.equals("authorization") || 
                name.equals("x-amz-date") || 
                name.equals("host") ||
                name.equals("x-amz-content-sha256")) {
                newRequestBuilder.removeHeader(pair.getFirst());
                logger.debug("Header removido: {}", pair.getFirst());
            }
        });
        
        // Agregar headers firmados
        awsRequest.getHeaders().forEach((key, value) -> {
            newRequestBuilder.header(key, value);
        });
        
        Request signedRequest = newRequestBuilder.build();
        logger.info("Request firmado - URL final: {}", signedRequest.url());
        logger.info("=== Fin AWS Signature V4 Signing ===");
        
        return signedRequest;
    }
    
    /**
     * Extrae el path S3 (bucket/key) del path completo del endpoint
     * 
     * Para Supabase Storage S3 API, probamos primero con el path completo
     * ya que puede requerir el prefijo /storage/v1/s3/ en la firma.
     */
    private String extractS3ResourcePath(String fullPath) {
        logger.info("Extrayendo resource path de: {}", fullPath);
        
        // Para Supabase Storage S3 API, usar el path completo
        // Esto incluye el prefijo /storage/v1/s3/ que Supabase puede requerir
        if (fullPath.startsWith("/storage/v1/s3/")) {
            logger.info("Usando path completo para la firma (incluye prefijo /storage/v1/s3/): {}", fullPath);
            return fullPath;
        }
        
        // Si no tiene el prefijo esperado, retornar el path completo
        logger.warn("Path no tiene el formato esperado /storage/v1/s3/, usando path completo: {}", fullPath);
        return fullPath;
    }
}

