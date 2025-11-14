package com.paxtech.utime.platform.reservations.infrastructure.payment.stripe;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.Payment;
import com.paxtech.utime.platform.reservations.domain.model.commands.CreatePaymentLinkCommand;
import com.paxtech.utime.platform.reservations.domain.services.StripePaymentService;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.PaymentRepository;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.ReservationRepository;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentLinkCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripePaymentServiceImpl implements StripePaymentService {

    private static final Logger logger = LoggerFactory.getLogger(StripePaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final String webhookSecret;

    public StripePaymentServiceImpl(
            PaymentRepository paymentRepository,
            ReservationRepository reservationRepository,
            @Value("${stripe.secret-key}") String secretKey,
            @Value("${stripe.webhook-secret}") String webhookSecret) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.webhookSecret = webhookSecret;
        
        // Configurar la clave secreta de Stripe
        Stripe.apiKey = secretKey;
        
        logger.info("StripePaymentService inicializado con secret key: {}...", 
                secretKey != null && secretKey.length() > 8 ? secretKey.substring(0, 8) + "..." : "null");
    }

    @Override
    public String createPaymentLink(Long paymentId, CreatePaymentLinkCommand command) {
        logger.info("=== Creando Payment Link en Stripe ===");
        logger.info("Payment ID: {}", paymentId);
        logger.info("Reservation ID: {}", command.reservationId());
        logger.info("Amount: {} {}", command.amount(), command.currency());

        try {
            // Obtener el Payment de la base de datos
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + paymentId));

            // Convertir el monto a centavos (Stripe usa centavos)
            long amountInCents = command.amount()
                    .multiply(BigDecimal.valueOf(100))
                    .longValue();

            logger.info("Amount in cents: {}", amountInCents);

            // Crear Payment Link en Stripe
            // Primero crear un Product
            com.stripe.param.ProductCreateParams productParams = 
                    com.stripe.param.ProductCreateParams.builder()
                            .setName(command.description() != null ? 
                                    command.description() : 
                                    "Reserva #" + command.reservationId())
                            .build();
            com.stripe.model.Product product = com.stripe.model.Product.create(productParams);
            
            // Luego crear un Price para ese Product
            com.stripe.param.PriceCreateParams priceParams = 
                    com.stripe.param.PriceCreateParams.builder()
                            .setCurrency(command.currency().toLowerCase())
                            .setUnitAmount(amountInCents)
                            .setProduct(product.getId())
                            .build();
            com.stripe.model.Price price = com.stripe.model.Price.create(priceParams);
            
            // Finalmente crear el Payment Link con el Price
            PaymentLinkCreateParams params = PaymentLinkCreateParams.builder()
                    .addLineItem(
                            PaymentLinkCreateParams.LineItem.builder()
                                    .setPrice(price.getId())
                                    .setQuantity(1L)
                                    .build()
                    )
                    .putMetadata("reservationId", command.reservationId().toString())
                    .putMetadata("clientId", command.clientId().toString())
                    .putMetadata("paymentId", paymentId.toString())
                    .build();

            PaymentLink paymentLink = PaymentLink.create(params);

            logger.info("Payment Link creado exitosamente");
            logger.info("Payment Link ID: {}", paymentLink.getId());
            logger.info("Payment Link URL: {}", paymentLink.getUrl());

            // Actualizar el Payment con el ID del Payment Link
            payment.updateStripePaymentLinkId(paymentLink.getId());
            paymentRepository.save(payment);

            logger.info("=== Payment Link creado y guardado ===");
            return paymentLink.getUrl();

        } catch (Exception e) {
            logger.error("Error al crear Payment Link en Stripe: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear Payment Link: " + e.getMessage(), e);
        }
    }

    @Override
    public void handleWebhook(String payload, String signature) {
        logger.info("=== Recibiendo webhook de Stripe ===");
        logger.info("Signature: {}...", signature != null && signature.length() > 20 ? signature.substring(0, 20) + "..." : "null");

        Event event;

        try {
            // Verificar que el webhook viene realmente de Stripe
            event = Webhook.constructEvent(payload, signature, webhookSecret);
            logger.info("Webhook verificado exitosamente");
            logger.info("Event type: {}", event.getType());
        } catch (SignatureVerificationException e) {
            logger.error("Error al verificar la firma del webhook: {}", e.getMessage());
            throw new RuntimeException("Invalid webhook signature", e);
        } catch (Exception e) {
            logger.error("Error al procesar el webhook: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing webhook: " + e.getMessage(), e);
        }

        // Procesar según el tipo de evento
        switch (event.getType()) {
            case "checkout.session.completed":
                handleCheckoutSessionCompleted(event);
                break;
            case "checkout.session.async_payment_succeeded":
                handleCheckoutSessionAsyncPaymentSucceeded(event);
                break;
            case "checkout.session.async_payment_failed":
                handleCheckoutSessionAsyncPaymentFailed(event);
                break;
            default:
                logger.info("Evento no manejado: {}", event.getType());
        }
    }

    private void handleCheckoutSessionCompleted(Event event) {
        logger.info("=== Procesando checkout.session.completed ===");
        
        try {
            // Deserializar el objeto Session del evento
            Session session = (Session) event.getDataObjectDeserializer()
                    .deserializeUnsafe();

            if (session == null) {
                logger.error("No se pudo deserializar la sesión del webhook");
                return;
            }

            logger.info("Checkout Session ID: {}", session.getId());
            logger.info("Payment Status: {}", session.getPaymentStatus());
            logger.info("Metadata: {}", session.getMetadata());

            // Obtener metadata
            String paymentIdStr = session.getMetadata().get("paymentId");
            String reservationIdStr = session.getMetadata().get("reservationId");

            if (paymentIdStr == null) {
                logger.error("Payment ID no encontrado en metadata del webhook");
                return;
            }

            Long paymentId = Long.parseLong(paymentIdStr);
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

            // Actualizar Payment
            payment.updateStripeCheckoutSessionId(session.getId());
            
            if (session.getPaymentIntent() != null) {
                String paymentIntentId = session.getPaymentIntent().toString();
                payment.updateStripePaymentIntentId(paymentIntentId);
            }

            // Verificar el estado del pago
            if ("paid".equals(session.getPaymentStatus())) {
                payment.markAsSucceeded();
                logger.info("Pago marcado como exitoso");

                // Actualizar estado de la reserva si existe
                if (reservationIdStr != null) {
                    Long reservationId = Long.parseLong(reservationIdStr);
                    reservationRepository.findById(reservationId).ifPresent(reservation -> {
                        // Aquí podrías agregar lógica para actualizar el estado de la reserva
                        logger.info("Reserva {} asociada al pago exitoso", reservationId);
                    });
                }
            } else {
                payment.markAsFailed();
                logger.warn("Pago no completado. Status: {}", session.getPaymentStatus());
            }

            paymentRepository.save(payment);
            logger.info("=== Webhook procesado exitosamente ===");

        } catch (Exception e) {
            logger.error("Error al procesar checkout.session.completed: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing checkout session: " + e.getMessage(), e);
        }
    }

    private void handleCheckoutSessionAsyncPaymentSucceeded(Event event) {
        logger.info("=== Procesando checkout.session.async_payment_succeeded ===");
        handleCheckoutSessionCompleted(event); // Mismo manejo que el evento normal
    }

    private void handleCheckoutSessionAsyncPaymentFailed(Event event) {
        logger.info("=== Procesando checkout.session.async_payment_failed ===");
        
        try {
            // Deserializar el objeto Session del evento
            Session session = (Session) event.getDataObjectDeserializer()
                    .deserializeUnsafe();

            if (session == null) {
                logger.error("No se pudo deserializar la sesión del webhook");
                return;
            }

            String paymentIdStr = session.getMetadata().get("paymentId");
            if (paymentIdStr == null) {
                logger.error("Payment ID no encontrado en metadata");
                return;
            }

            Long paymentId = Long.parseLong(paymentIdStr);
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

            payment.markAsFailed();
            paymentRepository.save(payment);

            logger.info("Pago marcado como fallido");

        } catch (Exception e) {
            logger.error("Error al procesar checkout.session.async_payment_failed: {}", e.getMessage(), e);
        }
    }
}

