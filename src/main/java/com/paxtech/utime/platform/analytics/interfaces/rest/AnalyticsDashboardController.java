package com.paxtech.utime.platform.analytics.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAllAnalyticsEventsQuery;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAnalyticsSummaryQuery;
import com.paxtech.utime.platform.analytics.domain.services.AnalyticsEventQueryService;
import com.paxtech.utime.platform.analytics.interfaces.rest.transform.AnalyticsEventResourceFromEntityAssembler;
import com.paxtech.utime.platform.analytics.interfaces.rest.transform.AnalyticsSummaryResourceFromEntityAssembler;
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
import java.util.Map;

/**
 * Serves a self-contained HTML analytics dashboard.
 * <p>
 * The page is public (it lives under the permitted {@code /api/v1/analytics/**}
 * path). The controller queries the analytics <b>in-process</b> (no HTTP round
 * trips between endpoints) and embeds the summary + recent events directly into
 * the page, so the browser makes a single request and the dashboard renders
 * atomically — no client-side fetch race against Azure cold starts. If the
 * in-process query fails, the page falls back to fetching the JSON endpoints.
 */
@RestController
@Tag(name = "Analytics", description = "Public experiment tracking events (UE01-UE05)")
public class AnalyticsDashboardController {

    /** Placeholder in dashboard.html replaced with the embedded JSON bootstrap. */
    private static final String BOOTSTRAP_TOKEN = "/*__ANALYTICS_BOOTSTRAP__*/null";

    /** How many recent events to embed (the table shows the latest 30). */
    private static final int EMBEDDED_EVENTS_LIMIT = 50;

    private final AnalyticsEventQueryService queryService;
    private final ObjectMapper objectMapper;

    public AnalyticsDashboardController(AnalyticsEventQueryService queryService, ObjectMapper objectMapper) {
        this.queryService = queryService;
        this.objectMapper = objectMapper;
    }

    @GetMapping(value = "/api/v1/analytics/dashboard", produces = MediaType.TEXT_HTML_VALUE)
    @Operation(summary = "Analytics dashboard (HTML)",
            description = "Public, self-contained HTML page with the analytics summary and recent events embedded server-side.")
    public ResponseEntity<String> dashboard() throws IOException {
        var resource = new ClassPathResource("analytics/dashboard.html");
        String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        html = html.replace(BOOTSTRAP_TOKEN, buildBootstrapLiteral());
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    /**
     * Builds the JavaScript literal injected in place of {@link #BOOTSTRAP_TOKEN}.
     * On any failure returns {@code "null"} so the page falls back to client fetch.
     */
    private String buildBootstrapLiteral() {
        try {
            var summary = AnalyticsSummaryResourceFromEntityAssembler.toResourceFromEntity(
                    queryService.handle(new GetAnalyticsSummaryQuery()));
            var events = queryService.handle(GetAllAnalyticsEventsQuery.all()).stream()
                    .limit(EMBEDDED_EVENTS_LIMIT)
                    .map(AnalyticsEventResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();

            String json = objectMapper.writeValueAsString(Map.of("summary", summary, "events", events));
            // Escapar '<' para no romper el bloque <script> (evita cerrar </script> desde datos).
            return json.replace("<", "\\u003c");
        } catch (Exception e) {
            return "null";
        }
    }
}
