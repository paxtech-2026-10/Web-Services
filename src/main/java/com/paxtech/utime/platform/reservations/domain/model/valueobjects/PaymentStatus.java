package com.paxtech.utime.platform.reservations.domain.model.valueobjects;

public enum PaymentStatus {
    PENDING,      // Pago pendiente (Payment Link creado, esperando pago)
    SUCCEEDED,    // Pago exitoso
    FAILED,       // Pago fallido
    CANCELED      // Pago cancelado
}

