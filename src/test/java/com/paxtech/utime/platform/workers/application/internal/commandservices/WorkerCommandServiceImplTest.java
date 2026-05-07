package com.paxtech.utime.platform.workers.application.internal.commandservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.interfaces.acl.ProviderContextFacade;
import com.paxtech.utime.platform.workers.domain.model.aggregates.Worker;
import com.paxtech.utime.platform.workers.domain.model.commands.CreateWorkerCommand;
import com.paxtech.utime.platform.workers.domain.model.commands.DeleteWorkerCommand;
import com.paxtech.utime.platform.workers.domain.model.commands.UpdateWorkerCommand;
import com.paxtech.utime.platform.workers.domain.model.valueobjects.ProviderId;
import com.paxtech.utime.platform.workers.domain.model.valueobjects.WorkerName;
import com.paxtech.utime.platform.workers.infrastructure.persistence.jpa.repositories.WorkerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkerCommandServiceImplTest {

    @Mock
    WorkerRepository workerRepository;

    @Mock
    ProviderContextFacade providerContextFacade;

    @InjectMocks
    WorkerCommandServiceImpl workerCommandService;

    private CreateWorkerCommand validCreateCommand;

    @BeforeEach
    void setUp() {
        validCreateCommand = new CreateWorkerCommand(
                "Ana",
                "Estilista",
                "https://cdn.example/a.jpg",
                10L
        );
    }

    @Test
    void createWorkerOkTest() {
        // Arrange
        when(workerRepository.existsByNameAndProviderId(any(WorkerName.class), any(ProviderId.class)))
                .thenReturn(false);
        when(providerContextFacade.fetchProviderById(10L)).thenReturn(Optional.of(mock(Provider.class)));

        // Act
        Optional<Worker> result = workerCommandService.handle(validCreateCommand);

        // Assert
        assertThat(result).isPresent();
        ArgumentCaptor<Worker> captor = ArgumentCaptor.forClass(Worker.class);
        verify(workerRepository).save(captor.capture());
        Worker saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Ana");
        assertThat(saved.getSalonId()).isEqualTo(10L);
    }

    @Test
    void createWorkerDuplicateTest() {
        // Arrange
        when(workerRepository.existsByNameAndProviderId(any(WorkerName.class), any(ProviderId.class)))
                .thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> workerCommandService.handle(validCreateCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(workerRepository, never()).save(any());
        verify(providerContextFacade, never()).fetchProviderById(any());
    }

    @Test
    void createWorkerMissingProviderTest() {
        // Arrange
        when(workerRepository.existsByNameAndProviderId(any(WorkerName.class), any(ProviderId.class)))
                .thenReturn(false);
        when(providerContextFacade.fetchProviderById(10L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> workerCommandService.handle(validCreateCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Provider");

        verify(workerRepository, never()).save(any());
    }

    @Test
    void updateWorkerOkTest() {
        // Arrange
        Worker existing = new Worker(validCreateCommand);
        UpdateWorkerCommand command = new UpdateWorkerCommand(1L, "Nuevo", "Barbero", "https://cdn.example/n.jpg", 10L);
        when(workerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(workerRepository.save(any(Worker.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<Worker> result = workerCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Nuevo");
        assertThat(result.get().getSpecialization()).isEqualTo("Barbero");
        verify(workerRepository).save(any(Worker.class));
    }

    @Test
    void updateWorkerNotFoundTest() {
        // Arrange
        UpdateWorkerCommand command = new UpdateWorkerCommand(99L, "X", "Y", "https://cdn.example/z.jpg", 1L);
        when(workerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> workerCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");

        verify(workerRepository, never()).save(any());
    }

    @Test
    void deleteWorkerOkTest() {
        // Arrange
        DeleteWorkerCommand command = new DeleteWorkerCommand(5L);
        when(workerRepository.existsById(5L)).thenReturn(true);

        // Act
        workerCommandService.handle(command);

        // Assert
        verify(workerRepository).deleteById(5L);
    }

    @Test
    void deleteWorkerNotFoundTest() {
        // Arrange
        DeleteWorkerCommand command = new DeleteWorkerCommand(5L);
        when(workerRepository.existsById(5L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> workerCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");

        verify(workerRepository, never()).deleteById(any());
    }
}
