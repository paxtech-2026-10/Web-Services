package com.paxtech.utime.platform.analytics.interfaces.rest.resources;

/**
 * Response body with the number of events recorded for a given event type.
 */
public record EventTypeCountResource(String eventType, long total) {
}
