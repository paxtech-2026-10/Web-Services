package com.paxtech.utime.platform.reservations.domain.services;

import com.paxtech.utime.platform.reservations.domain.model.commands.CreatePaymentLinkCommand;

public interface StripePaymentService {
    /**
     * Crea un Payment Link en Stripe y lo asocia con un Payment existente
     * @param paymentId ID del Payment en la base de datos
     * @param command Comando con la información del pago
     * @return URL del Payment Link de Stripe
     */
    String createPaymentLink(Long paymentId, CreatePaymentLinkCommand command);

    /**
     * Maneja un webhook de Stripe
     * @param payload Cuerpo del webhook (JSON)
     * @param signature Firma del webhook para verificación
     */
    void handleWebhook(String payload, String signature);
}

