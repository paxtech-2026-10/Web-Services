package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllProvidersQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderByIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderByProfileIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderByUserIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * @summary
 * Interface for query operations on the Salons aggregate.
 * Defines contracts for retrieving salon data.
 */
public interface ProviderQueryService {

    /**
     * Handle retrieving all salons.
     * @param query The query with no parameters.
     * @return List of salons.
     */
    List<Provider> handle(GetAllProvidersQuery query);

    /**
     * Handle retrieving a salon by its ID.
     * @param query Query containing the salon ID.
     * @return Optional salon.
     */
    Optional<Provider> handle(GetProviderByIdQuery query);

    /**
     * Handle retrieving a salon by its email.
     * @param query Query containing the email.
     * @return Optional salon.
     */
    Optional<Provider> handle(GetProviderByProfileIdQuery query);

    Optional<Provider> handle(GetProviderByUserIdQuery query);
}
