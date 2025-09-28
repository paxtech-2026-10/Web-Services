package com.paxtech.utime.platform.reservations.domain.model.commands;

public record CreatePaymentCommand(float amount, String currency, boolean status) {
}
