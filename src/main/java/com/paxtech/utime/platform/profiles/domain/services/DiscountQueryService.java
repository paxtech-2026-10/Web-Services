package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Discount;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllDiscountsByProviderProfileIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllDiscountsQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetDiscountByIdQuery;

import java.util.List;
import java.util.Optional;

public interface DiscountQueryService {
    Optional<Discount> handle(GetDiscountByIdQuery query);
    List<Discount> handle(GetAllDiscountsByProviderProfileIdQuery query);
    List<Discount> handle(GetAllDiscountsQuery query);
}

