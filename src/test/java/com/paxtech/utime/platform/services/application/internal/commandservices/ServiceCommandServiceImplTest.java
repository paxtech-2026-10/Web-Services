package com.paxtech.utime.platform.services.application.internal.commandservices;

import com.paxtech.utime.platform.services.domain.model.aggregates.Service;
import com.paxtech.utime.platform.services.domain.model.commands.CreateServiceCommand;
import com.paxtech.utime.platform.services.domain.model.commands.DeleteServiceCommand;
import com.paxtech.utime.platform.services.domain.model.commands.UpdateServiceCommand;
import com.paxtech.utime.platform.services.domain.model.valueobjects.Name;
import com.paxtech.utime.platform.services.domain.model.valueobjects.ProviderId;
import com.paxtech.utime.platform.services.infrastructure.persistence.jpa.repositories.ServiceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceCommandServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceCommandServiceImpl serviceCommandService;

    // ========================================================
    // handle(CreateServiceCommand)
    // ========================================================

    @Test
    @DisplayName("handle(Create): saves and returns the created Service when no duplicate exists for the provider")
    void handleCreate_savesAndReturnsService_whenNoDuplicateExists() {
        // Arrange
        var command = new CreateServiceCommand("Haircut", 30, 5000L, true, 1L);
        when(serviceRepository.existsByProviderIdAndName(any(ProviderId.class), any(Name.class)))
                .thenReturn(false);

        // Act
        Optional<Service> result = serviceCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName().name()).isEqualTo("Haircut");
        assertThat(result.get().getDuration().duration()).isEqualTo(30);
        assertThat(result.get().getPrice().price()).isEqualTo(5000L);
        assertThat(result.get().getProviderId().providerId()).isEqualTo(1L);
        assertThat(result.get().getStatus().status()).isTrue();
        verify(serviceRepository).save(any(Service.class));
    }

    @Test
    @DisplayName("handle(Create): throws IllegalArgumentException when a Service with the same name already exists for the provider")
    void handleCreate_throws_whenServiceWithSameNameExistsForProvider() {
        // Arrange
        var command = new CreateServiceCommand("Haircut", 30, 5000L, true, 1L);
        when(serviceRepository.existsByProviderIdAndName(any(ProviderId.class), any(Name.class)))
                .thenReturn(true);

        // Act + Assert
        assertThatThrownBy(() -> serviceCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
        verify(serviceRepository, never()).save(any(Service.class));
    }

    // ========================================================
    // handle(DeleteServiceCommand)
    // ========================================================

    @Test
    @DisplayName("handle(Delete): deletes by id when the Service exists")
    void handleDelete_deletes_whenServiceExists() {
        // Arrange
        var command = new DeleteServiceCommand(42L);
        when(serviceRepository.existsById(42L)).thenReturn(true);

        // Act
        serviceCommandService.handle(command);

        // Assert
        verify(serviceRepository).deleteById(42L);
    }

    @Test
    @DisplayName("handle(Delete): throws IllegalArgumentException when the Service does not exist")
    void handleDelete_throws_whenServiceDoesNotExist() {
        // Arrange
        var command = new DeleteServiceCommand(42L);
        when(serviceRepository.existsById(42L)).thenReturn(false);

        // Act + Assert
        assertThatThrownBy(() -> serviceCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not exist");
        verify(serviceRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("handle(Delete): wraps repository errors during deletion in IllegalArgumentException")
    void handleDelete_wrapsRepositoryError_whenDeletionFails() {
        // Arrange
        var command = new DeleteServiceCommand(42L);
        when(serviceRepository.existsById(42L)).thenReturn(true);
        doThrow(new RuntimeException("DB down")).when(serviceRepository).deleteById(42L);

        // Act + Assert
        assertThatThrownBy(() -> serviceCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Error while deleting service");
    }

    // ========================================================
    // handle(UpdateServiceCommand)
    // ========================================================

    @Test
    @DisplayName("handle(Update): updates name/duration/price and leaves status & providerId untouched")
    void handleUpdate_updatesService_whenServiceExists() {
        // Arrange
        var existing = new Service(new CreateServiceCommand("Old", 20, 1000L, true, 1L));
        var command = new UpdateServiceCommand(7L, "New", 45, 9999L);
        when(serviceRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(serviceRepository.save(any(Service.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Optional<Service> result = serviceCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName().name()).isEqualTo("New");
        assertThat(result.get().getDuration().duration()).isEqualTo(45);
        assertThat(result.get().getPrice().price()).isEqualTo(9999L);
        assertThat(result.get().getStatus().status()).isTrue();
        assertThat(result.get().getProviderId().providerId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("handle(Update): throws IllegalArgumentException when the Service does not exist")
    void handleUpdate_throws_whenServiceDoesNotExist() {
        // Arrange
        var command = new UpdateServiceCommand(7L, "New", 45, 9999L);
        when(serviceRepository.findById(7L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> serviceCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not exist");
        verify(serviceRepository, never()).save(any(Service.class));
    }

    @Test
    @DisplayName("handle(Update): lets persistence errors propagate without wrapping them")
    void handleUpdate_propagatesRepositoryError_whenSaveFails() {
        // Arrange
        // Persistence failures are infrastructure errors, not client errors, so the original
        // exception must propagate to the boundary instead of being swallowed and re-thrown
        // as an IllegalArgumentException.
        var existing = new Service(new CreateServiceCommand("Old", 20, 1000L, true, 1L));
        var command = new UpdateServiceCommand(7L, "New", 45, 9999L);
        var dbError = new RuntimeException("DB down");
        when(serviceRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(serviceRepository.save(any(Service.class))).thenThrow(dbError);

        // Act + Assert
        assertThatThrownBy(() -> serviceCommandService.handle(command))
                .isSameAs(dbError);
    }
}
