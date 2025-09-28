package com.paxtech.utime.platform.reservations.application.internal.queryservices;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.Payment;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetAllPaymentsQuery;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetPaymentByIdQuery;
import com.paxtech.utime.platform.reservations.domain.services.PaymentQueryService;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {
    private final PaymentRepository paymentRepository;

    public PaymentQueryServiceImpl(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Optional<Payment> handle(GetPaymentByIdQuery query){
        return paymentRepository.findById(query.id());
    }

    @Override
    public List<Payment> handle(GetAllPaymentsQuery query){
        return paymentRepository.findAll();
    }
}
