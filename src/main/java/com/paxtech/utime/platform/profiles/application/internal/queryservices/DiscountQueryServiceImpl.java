package com.paxtech.utime.platform.profiles.application.internal.queryservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Discount;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllDiscountsByProviderProfileIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllDiscountsQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetDiscountByIdQuery;
import com.paxtech.utime.platform.profiles.domain.services.DiscountQueryService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.DiscountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountQueryServiceImpl implements DiscountQueryService {

    private final DiscountRepository discountRepository;

    public DiscountQueryServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public Optional<Discount> handle(GetDiscountByIdQuery query) {
        return discountRepository.findById(query.id());
    }

    @Override
    public List<Discount> handle(GetAllDiscountsByProviderProfileIdQuery query) {
        return discountRepository.findAllByProviderProfileId(query.providerProfileId());
    }

    @Override
    public List<Discount> handle(GetAllDiscountsQuery query) {
        return discountRepository.findAll();
    }
}

