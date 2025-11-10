package com.paxtech.utime.platform.profiles.domain.model.queries;

import com.paxtech.utime.platform.profiles.domain.model.valueobjects.CompanyName;

public record GetProviderProfilesByCompanyNameQuery(CompanyName companyName) {
}
