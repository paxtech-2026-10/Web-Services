package com.paxtech.utime.platform.services.domain.services;

import com.paxtech.utime.platform.services.domain.model.aggregates.Service;
import com.paxtech.utime.platform.services.domain.model.queries.GetAllServicesQuery;
import com.paxtech.utime.platform.services.domain.model.queries.GetServiceByIdQuery;
import com.paxtech.utime.platform.services.domain.model.queries.GetServicesBySalonIdQuery;

import java.util.List;
import java.util.Optional;

public interface ServiceQueryService {
    Optional<Service> handle(GetServiceByIdQuery query);

    List<Service> handle(GetServicesBySalonIdQuery query);

    List<Service> handle(GetAllServicesQuery query);

}
