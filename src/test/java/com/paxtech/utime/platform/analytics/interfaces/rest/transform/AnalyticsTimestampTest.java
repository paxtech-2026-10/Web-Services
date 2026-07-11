package com.paxtech.utime.platform.analytics.interfaces.rest.transform;

import com.paxtech.utime.platform.analytics.domain.model.aggregates.AnalyticsEvent;
import com.paxtech.utime.platform.analytics.domain.model.commands.RecordAnalyticsEventCommand;
import com.paxtech.utime.platform.analytics.domain.model.queries.AnalyticsSummary;
import com.paxtech.utime.platform.analytics.domain.model.valueobjects.ActorType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyticsTimestampTest {

    @Test
    void eventTimestampIsReturnedAsUtcInstant() {
        var before = Instant.now();
        var event = new AnalyticsEvent(new RecordAnalyticsEventCommand(
                "profile_view", ActorType.CLIENT, 1L, 2L, null, Map.of()));

        var resource = AnalyticsEventResourceFromEntityAssembler.toResourceFromEntity(event);
        var occurredAt = Instant.parse(resource.occurredAt());

        assertThat(resource.occurredAt()).endsWith("Z");
        assertThat(occurredAt).isBetween(before, Instant.now());
    }

    @Test
    void summaryTimestampsAreReturnedWithUtcOffset() {
        var summary = new AnalyticsSummary(
                1L,
                1L,
                LocalDateTime.of(2026, 7, 10, 23, 30),
                LocalDateTime.of(2026, 7, 11, 1, 44),
                List.of(),
                List.of());

        var resource = AnalyticsSummaryResourceFromEntityAssembler.toResourceFromEntity(summary);

        assertThat(resource.firstEventAt()).isEqualTo("2026-07-10T23:30:00Z");
        assertThat(resource.lastEventAt()).isEqualTo("2026-07-11T01:44:00Z");
        assertThat(Instant.parse(resource.lastEventAt()).atOffset(ZoneOffset.ofHours(-5)).getDayOfMonth())
                .isEqualTo(10);
    }
}
