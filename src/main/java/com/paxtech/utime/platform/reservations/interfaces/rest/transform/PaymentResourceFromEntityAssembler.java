package com.paxtech.utime.platform.reservations.interfaces.rest.transform;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.Payment;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.PaymentResource;

public class PaymentResourceFromEntityAssembler {
    public static PaymentResource toResourceFromEntity(Payment entity){
        return new PaymentResource(
                entity.getId(),
                entity.getMoney().amount(),
                entity.getMoney().currency(),
                entity.isStatus()
        );
    }
}
