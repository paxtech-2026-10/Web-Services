package com.paxtech.utime.platform.reservations.domain.services;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.Payment;
import com.paxtech.utime.platform.reservations.domain.model.commands.CreatePaymentCommand;

import java.util.Optional;

public interface PaymentCommandService {
    Optional<Payment> handle(CreatePaymentCommand command);
}
