package com.paxtech.utime.platform.services.domain.exception;

public class SalonNotFoundException extends RuntimeException {
    public SalonNotFoundException(Long salonId) {
        super(String.format("Salon with Id %s not found:", salonId));
    }
}
