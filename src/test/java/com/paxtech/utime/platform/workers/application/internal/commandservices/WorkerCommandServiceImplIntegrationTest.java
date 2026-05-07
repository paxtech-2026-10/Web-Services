package com.paxtech.utime.platform.workers.application.internal.commandservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.interfaces.acl.ProviderContextFacade;
import com.paxtech.utime.platform.workers.domain.model.commands.CreateWorkerCommand;
import com.paxtech.utime.platform.workers.domain.model.commands.DeleteWorkerCommand;
import com.paxtech.utime.platform.workers.domain.model.commands.UpdateWorkerCommand;
import com.paxtech.utime.platform.workers.infrastructure.persistence.jpa.repositories.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(WorkerCommandServiceImplIntegrationTest.Config.class)
class WorkerCommandServiceImplIntegrationTest {

    @Autowired
    private WorkerCommandServiceImpl service;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @MockBean
    private ProviderContextFacade providerContextFacade;

    @TestConfiguration
    static class Config {
        @Bean
        WorkerCommandServiceImpl workerCommandService(WorkerRepository workerRepository,
                                                      ProviderContextFacade providerContextFacade) {
            return new WorkerCommandServiceImpl(workerRepository, providerContextFacade);
        }
    }

    @Test
    void createWorkerIntegrationTest() {
        // Arrange
        CreateWorkerCommand command = new CreateWorkerCommand("Ana", "Estilista", "https://paxtech.examplea.jpg", 10L);
        when(providerContextFacade.fetchProviderById(10L)).thenReturn(Optional.of(mock(Provider.class)));

        // Act
        var result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        entityManager.flush();
        entityManager.clear();
        var all = workerRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("Ana");
        assertThat(all.get(0).getSalonId()).isEqualTo(10L);
    }

    @Test
    void updateWorkerIntegrationTest() {
        // Arrange
        var saved = workerRepository.save(new com.paxtech.utime.platform.workers.domain.model.aggregates.Worker(
                new CreateWorkerCommand("Luis", "Barbero", "https://paxtech.examplel.jpg", 2L)
        ));
        UpdateWorkerCommand command = new UpdateWorkerCommand(
                saved.getId(),
                "Luis Updated",
                "Colorista",
                "https://paxtech.examplenew.jpg",
                3L
        );

        // Act
        var result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        entityManager.flush();
        entityManager.clear();
        var updated = workerRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Luis Updated");
        assertThat(updated.getSpecialization()).isEqualTo("Colorista");
        assertThat(updated.getSalonId()).isEqualTo(3L);
    }

    @Test
    void deleteWorkerIntegrationTest() {
        // Arrange
        var saved = workerRepository.save(new com.paxtech.utime.platform.workers.domain.model.aggregates.Worker(
                new CreateWorkerCommand("Mario", "Barbero", "https://paxtech.examplem.jpg", 4L)
        ));
        DeleteWorkerCommand command = new DeleteWorkerCommand(saved.getId());

        // Act
        service.handle(command);

        // Assert
        entityManager.flush();
        entityManager.clear();
        assertThat(workerRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void createWorkerDuplicateNameIntegrationTest() {
        // Arrange
        workerRepository.save(new com.paxtech.utime.platform.workers.domain.model.aggregates.Worker(
                new CreateWorkerCommand("Ana", "Barbero", "https://paxtech.exampleone.jpg", 15L)
        ));
        CreateWorkerCommand duplicated = new CreateWorkerCommand("Ana", "Estilista", "https://paxtech.exampletwo.jpg", 15L);

        // Act & Assert
        assertThatThrownBy(() -> service.handle(duplicated))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }
}
