package com.paxtech.utime.platform.profiles.domain.services;


import com.paxtech.utime.platform.profiles.domain.model.aggregates.Client;
import com.paxtech.utime.platform.profiles.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * @summary
 * Interface for query operations on the Clients aggregate.
 * Defines contracts for retrieving salon data.
 */

public interface ClientQueryService {
    /**
     * Handle retrieving all salons.
     * @param query The query with no parameters.
     * @return List of salons.
     */
    List<Client> handle(GetAllClientsQuery query);

    /**
     * Handle retrieving a salon by its ID.
     * @param query Query containing the salon ID.
     * @return Optional salon.
     */
    Optional<Client> handle(GetClientByIdQuery query);

    Optional<Client> handle(GetClientByUserIdQuery query);

}
