package com.paxtech.utime.platform.reservations.infrastructure.payment.stripe;

import com.paxtech.utime.platform.reservations.domain.model.commands.CreatePaymentLinkCommand;
import com.paxtech.utime.platform.reservations.domain.services.StripePaymentService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class NoOpStripePaymentService implements StripePaymentService {
    @Override
    public String createPaymentLink(Long paymentId, CreatePaymentLinkCommand command) {
        return "test://stripe/payment-link/" + paymentId;
    }

    @Override
    public void handleWebhook(String payload, String signature) {
    }
}
