package com.paxtech.utime.platform.reservations.domain.services;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.Payment;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetAllPaymentsQuery;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetPaymentByIdQuery;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryService {
    Optional<Payment> handle(GetPaymentByIdQuery query);

    List<Payment> handle(GetAllPaymentsQuery query);
}
