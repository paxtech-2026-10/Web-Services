package com.paxtech.utime.platform.reservations.interfaces.rest.acl;

public record PaymentDto(Long id, float amount, String currency, boolean status) {
}
