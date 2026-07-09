package com.paxtech.utime.platform.reservations.interfaces.rest.resources;

/**
 * Compact payment view embedded in {@link ReservationDetailsResource} so the
 * client apps can show whether an appointment is paid.
 * <p>
 * {@code status} is {@code true} only when the payment succeeded, matching the
 * boolean the frontends read as "Pagado / No pagado".
 *
 * @param id       payment id (0 when the reservation has no payment yet)
 * @param amount   payment amount
 * @param currency payment currency
 * @param status   whether the payment succeeded
 */
public record PaymentSummaryDto(Long id, float amount, String currency, boolean status) {

    /** Placeholder used when a reservation has no associated payment. */
    public static PaymentSummaryDto none() {
        return new PaymentSummaryDto(0L, 0f, "", false);
    }
}
