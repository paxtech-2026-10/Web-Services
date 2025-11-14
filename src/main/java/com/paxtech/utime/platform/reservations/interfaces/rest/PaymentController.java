package com.paxtech.utime.platform.reservations.interfaces.rest;

import com.paxtech.utime.platform.reservations.domain.model.commands.CreatePaymentCommand;
import com.paxtech.utime.platform.reservations.domain.model.commands.CreatePaymentLinkCommand;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetAllPaymentsQuery;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetPaymentByIdQuery;
import com.paxtech.utime.platform.reservations.domain.services.PaymentCommandService;
import com.paxtech.utime.platform.reservations.domain.services.PaymentQueryService;
import com.paxtech.utime.platform.reservations.domain.services.StripePaymentService;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.PaymentRepository;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.CreatePaymentLinkResource;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.CreatePaymentResource;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.PaymentLinkResponse;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.PaymentResource;
import com.paxtech.utime.platform.reservations.interfaces.rest.transform.CreatePaymentCommandFromResourceAssembler;
import com.paxtech.utime.platform.reservations.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
import com.paxtech.utime.platform.shared.interfaces.rest.resources.MessageResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PaymentController
 */
@RestController
@RequestMapping(value = "api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payments", description = "Available Payment Endpoints")
public class PaymentController {
    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;
    private final StripePaymentService stripePaymentService;
    private final PaymentRepository paymentRepository;

    public PaymentController(
            PaymentCommandService paymentCommandService,
            PaymentQueryService paymentQueryService,
            StripePaymentService stripePaymentService,
            PaymentRepository paymentRepository) {
        this.paymentCommandService = paymentCommandService;
        this.paymentQueryService = paymentQueryService;
        this.stripePaymentService = stripePaymentService;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Create a new payment
     * @param resource The {@link CreatePaymentResource} instance
     * @return A {@link PaymentResource} or 400 if invalid
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<PaymentResource> createPayment(@RequestBody CreatePaymentResource resource) {
        CreatePaymentCommand command = CreatePaymentCommandFromResourceAssembler.toCommandFromResource(resource);
        var payment = paymentCommandService.handle(command);
        if (payment.isEmpty()) return ResponseEntity.badRequest().build();
        var paymentResource = PaymentResourceFromEntityAssembler.toResourceFromEntity(payment.get());
        return new ResponseEntity<>(paymentResource, HttpStatus.CREATED);
    }

    /**
     * Get a payment by ID
     * @param paymentId The ID of the payment
     * @return A {@link PaymentResource} or 404 if not found
     */
    @GetMapping("/{paymentId}")
    @Operation(summary = "Get a payment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentResource> getPaymentById(@PathVariable Long paymentId) {
        var query = new GetPaymentByIdQuery(paymentId);
        var payment = paymentQueryService.handle(query);
        if (payment.isEmpty()) return ResponseEntity.notFound().build();
        var resource = PaymentResourceFromEntityAssembler.toResourceFromEntity(payment.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Get all payments
     * @return A list of {@link PaymentResource} or 404 if empty
     */
    @GetMapping
    @Operation(summary = "Get all payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments found"),
            @ApiResponse(responseCode = "404", description = "No payments found")
    })
    public ResponseEntity<List<PaymentResource>> getAllPayments() {
        var payments = paymentQueryService.handle(new GetAllPaymentsQuery());
        if (payments.isEmpty()) return ResponseEntity.notFound().build();
        var resources = payments.stream()
                .map(PaymentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Create a Stripe Payment Link for an existing payment
     * @param resource The {@link CreatePaymentLinkResource} with payment details
     * @return A {@link PaymentLinkResponse} with the payment link URL
     */
    @PostMapping("/create-payment-link")
    @Operation(summary = "Create a Stripe Payment Link", description = "Creates a Stripe Payment Link for an existing payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment Link created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<?> createPaymentLink(@RequestBody CreatePaymentLinkResource resource) {
        try {
            // Verificar que el Payment existe
            var paymentOpt = paymentRepository.findById(resource.paymentId());
            if (paymentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResource("Payment not found"));
            }

            var payment = paymentOpt.get();

            // Crear el comando para Stripe
            var command = new CreatePaymentLinkCommand(
                    payment.getReservationId(),
                    payment.getClientId(),
                    resource.amount(),
                    resource.currency(),
                    resource.description() != null ? resource.description() : "Reserva #" + payment.getReservationId()
            );

            // Crear el Payment Link en Stripe
            String paymentLinkUrl = stripePaymentService.createPaymentLink(resource.paymentId(), command);

            return ResponseEntity.ok(new PaymentLinkResponse(paymentLinkUrl));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResource(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResource("Error creating payment link: " + e.getMessage()));
        }
    }

    /**
     * Webhook endpoint to receive Stripe events
     * @param payload The webhook payload (JSON)
     * @param signature The Stripe signature header for verification
     * @return 200 OK if processed successfully
     */
    @PostMapping("/webhook")
    @Operation(summary = "Stripe Webhook", description = "Receives webhook events from Stripe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Webhook processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid webhook signature")
    })
    public ResponseEntity<?> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {
        try {
            stripePaymentService.handleWebhook(payload, signature);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResource("Webhook processing failed: " + e.getMessage()));
        }
    }
}
