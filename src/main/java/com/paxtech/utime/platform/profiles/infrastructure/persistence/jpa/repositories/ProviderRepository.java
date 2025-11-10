package com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.iam.domain.model.aggregates.User;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.domain.model.valueobjects.CompanyName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    //Optional<Provider> findByProviderProfile_Id(Long providerProfileId);
    Optional<Provider> findByUserId(Long userId);

    List<Provider> findProvidersByCompanyName(CompanyName companyName);

    // Búsqueda parcial (contiene, case-insensitive) - para búsquedas flexibles
    @Query("SELECT p FROM Provider p WHERE LOWER(p.companyName.value) LIKE LOWER(CONCAT('%', :companyName, '%'))")
    List<Provider> findByCompanyNameContainingIgnoreCase(@Param("companyName") String companyName);
}
