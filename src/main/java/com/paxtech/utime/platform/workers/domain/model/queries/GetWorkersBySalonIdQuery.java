package com.paxtech.utime.platform.workers.domain.model.queries;

import com.paxtech.utime.platform.workers.domain.model.valueobjects.ProviderId;

public record GetWorkersBySalonIdQuery(ProviderId providerId) {
}
