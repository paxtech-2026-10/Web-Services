package com.paxtech.utime.platform.reservations.interfaces.rest.resources;

public record PaymentResource(Long id, float amount, String currency, boolean status) {
}
