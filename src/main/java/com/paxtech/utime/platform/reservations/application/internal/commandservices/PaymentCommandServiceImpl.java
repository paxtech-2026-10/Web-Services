package com.paxtech.utime.platform.reservations.application.internal.commandservices;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.Payment;
import com.paxtech.utime.platform.reservations.domain.model.commands.CreatePaymentCommand;
import com.paxtech.utime.platform.reservations.domain.services.PaymentCommandService;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {
    private final PaymentRepository paymentRepository;

    public PaymentCommandServiceImpl(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Optional<Payment> handle(CreatePaymentCommand command){
        var payment = new Payment(command);
        paymentRepository.save(payment);
        return Optional.of(payment);
    }
}
