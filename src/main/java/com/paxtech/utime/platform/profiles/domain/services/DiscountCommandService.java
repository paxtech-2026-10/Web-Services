package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Discount;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateDiscountCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteDiscountCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateDiscountCommand;

import java.util.Optional;

public interface DiscountCommandService {
    Optional<Discount> handle(CreateDiscountCommand command);
    Optional<Discount> handle(UpdateDiscountCommand command);
    void handle(DeleteDiscountCommand command);
}

