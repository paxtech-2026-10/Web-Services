package com.paxtech.utime.platform.analytics.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Serves a self-contained HTML analytics dashboard.
 * <p>
 * The page is public (it lives under the permitted {@code /api/v1/analytics/**}
 * path) and fetches the existing public JSON endpoints ({@code /summary} and
 * {@code /events}) client-side, so anyone with the link can view the analytics
 * on a professional page — no build tooling or authentication required.
 */
@RestController
@Tag(name = "Analytics", description = "Public experiment tracking events (UE01-UE05)")
public class AnalyticsDashboardController {

    @GetMapping(value = "/api/v1/analytics/dashboard", produces = MediaType.TEXT_HTML_VALUE)
    @Operation(summary = "Analytics dashboard (HTML)",
            description = "Public, self-contained HTML page that visualizes the analytics summary and recent events.")
    public ResponseEntity<String> dashboard() throws IOException {
        var resource = new ClassPathResource("analytics/dashboard.html");
        String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }
}
