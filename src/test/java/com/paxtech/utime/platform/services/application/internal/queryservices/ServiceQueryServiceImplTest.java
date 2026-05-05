package com.paxtech.utime.platform.services.application.internal.queryservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.interfaces.acl.ProviderContextFacade;
import com.paxtech.utime.platform.services.domain.exception.SalonNotFoundException;
import com.paxtech.utime.platform.services.domain.model.aggregates.Service;
import com.paxtech.utime.platform.services.domain.model.commands.CreateServiceCommand;
import com.paxtech.utime.platform.services.domain.model.queries.GetAllServicesQuery;
import com.paxtech.utime.platform.services.domain.model.queries.GetServiceByIdQuery;
import com.paxtech.utime.platform.services.domain.model.queries.GetServicesBySalonIdQuery;
import com.paxtech.utime.platform.services.domain.model.valueobjects.ProviderId;
import com.paxtech.utime.platform.services.infrastructure.persistence.jpa.repositories.ServiceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceQueryServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ProviderContextFacade providerContextFacade;

    @InjectMocks
    private ServiceQueryServiceImpl serviceQueryService;

    // ========================================================
    // handle(GetAllServicesQuery)
    // ========================================================

    @Test
    @DisplayName("handle(GetAll): returns the full list from the repository")
    void handleGetAll_returnsAllServices() {
        // Arrange
        var s1 = new Service(new CreateServiceCommand("Haircut", 30, 5000L, true, 1L));
        var s2 = new Service(new CreateServiceCommand("Manicure", 45, 7000L, true, 1L));
        when(serviceRepository.findAll()).thenReturn(List.of(s1, s2));

        // Act
        List<Service> result = serviceQueryService.handle(new GetAllServicesQuery());

        // Assert
        assertThat(result).hasSize(2).containsExactly(s1, s2);
    }

    @Test
    @DisplayName("handle(GetAll): returns an empty list when the repository has no Services")
    void handleGetAll_returnsEmptyList_whenNoServices() {
        // Arrange
        when(serviceRepository.findAll()).thenReturn(List.of());

        // Act
        List<Service> result = serviceQueryService.handle(new GetAllServicesQuery());

        // Assert
        assertThat(result).isEmpty();
    }

    // ========================================================
    // handle(GetServiceByIdQuery)
    // ========================================================

    @Test
    @DisplayName("handle(GetById): returns the Service when present")
    void handleGetById_returnsService_whenPresent() {
        // Arrange
        var existing = new Service(new CreateServiceCommand("Haircut", 30, 5000L, true, 1L));
        when(serviceRepository.findById(7L)).thenReturn(Optional.of(existing));

        // Act
        Optional<Service> result = serviceQueryService.handle(new GetServiceByIdQuery(7L));

        // Assert
        assertThat(result).isPresent().contains(existing);
    }

    @Test
    @DisplayName("handle(GetById): returns Optional.empty() when not found")
    void handleGetById_returnsEmpty_whenNotFound() {
        // Arrange
        when(serviceRepository.findById(7L)).thenReturn(Optional.empty());

        // Act
        Optional<Service> result = serviceQueryService.handle(new GetServiceByIdQuery(7L));

        // Assert
        assertThat(result).isEmpty();
    }

    // ========================================================
    // handle(GetServicesBySalonIdQuery)
    // ========================================================

    @Test
    @DisplayName("handle(GetBySalonId): returns the Services owned by the provider when the provider exists")
    void handleGetBySalonId_returnsServices_whenProviderExists() {
        // Arrange
        var providerId = new ProviderId(5L);
        var s1 = new Service(new CreateServiceCommand("Haircut", 30, 5000L, true, 5L));
        when(providerContextFacade.fetchProviderById(5L)).thenReturn(Optional.of(mock(Provider.class)));
        when(serviceRepository.findByProviderId(providerId)).thenReturn(List.of(s1));

        // Act
        List<Service> result = serviceQueryService.handle(new GetServicesBySalonIdQuery(providerId));

        // Assert
        assertThat(result).hasSize(1).containsExactly(s1);
    }

    @Test
    @DisplayName("handle(GetBySalonId): returns an empty list when the provider exists but has no Services")
    void handleGetBySalonId_returnsEmptyList_whenProviderHasNoServices() {
        // Arrange
        var providerId = new ProviderId(5L);
        when(providerContextFacade.fetchProviderById(5L)).thenReturn(Optional.of(mock(Provider.class)));
        when(serviceRepository.findByProviderId(providerId)).thenReturn(List.of());

        // Act
        List<Service> result = serviceQueryService.handle(new GetServicesBySalonIdQuery(providerId));

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("handle(GetBySalonId): throws SalonNotFoundException when the provider does not exist")
    void handleGetBySalonId_throwsSalonNotFound_whenProviderDoesNotExist() {
        // Arrange
        var providerId = new ProviderId(5L);
        when(providerContextFacade.fetchProviderById(5L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> serviceQueryService.handle(new GetServicesBySalonIdQuery(providerId)))
                .isInstanceOf(SalonNotFoundException.class)
                .hasMessageContaining("5");
    }
}
