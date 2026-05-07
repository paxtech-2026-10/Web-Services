package com.paxtech.utime.platform.workers.application.internal.queryservices;

import com.paxtech.utime.platform.workers.application.internal.queryservices.WorkerQueryServiceImpl;
import com.paxtech.utime.platform.workers.domain.model.aggregates.Worker;
import com.paxtech.utime.platform.workers.domain.model.commands.CreateWorkerCommand;
import com.paxtech.utime.platform.workers.domain.model.queries.GetAllWorkersQuery;
import com.paxtech.utime.platform.workers.domain.model.queries.GetWorkerByIdQuery;
import com.paxtech.utime.platform.workers.domain.model.queries.GetWorkersBySalonIdQuery;
import com.paxtech.utime.platform.workers.domain.model.valueobjects.ProviderId;
import com.paxtech.utime.platform.workers.infrastructure.persistence.jpa.repositories.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(WorkerQueryServiceImplIntegrationTest.Config.class)
class WorkerQueryServiceImplIntegrationTest {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private WorkerQueryServiceImpl service;

    @TestConfiguration
    static class Config {
        @Bean
        WorkerQueryServiceImpl workerQueryService(WorkerRepository workerRepository) {
            return new WorkerQueryServiceImpl(workerRepository);
        }
    }

    @Test
    void getBySalonIdIntegrationTest() {
        // Arrange
        workerRepository.save(new Worker(new CreateWorkerCommand("Ana", "Estilista", "https://paxtech.examplea.jpg", 9L)));
        workerRepository.save(new Worker(new CreateWorkerCommand("Luis", "Barbero", "https://paxtech.examplel.jpg", 9L)));
        workerRepository.save(new Worker(new CreateWorkerCommand("Mario", "Barbero", "https://paxtech.examplem.jpg", 7L)));
        GetWorkersBySalonIdQuery query = new GetWorkersBySalonIdQuery(new ProviderId(9L));

        // Act
        var result = service.handle(query);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Worker::getSalonId).containsOnly(9L);
    }

    @Test
    void getByIdIntegrationTest() {
        // Arrange
        Worker saved = workerRepository.save(new Worker(new CreateWorkerCommand("Ana", "Estilista", "https://paxtech.examplea.jpg", 6L)));
        GetWorkerByIdQuery query = new GetWorkerByIdQuery(saved.getId());

        // Act
        var result = service.handle(query);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Ana");
    }

    @Test
    void getAllWorkersIntegrationTest() {
        // Arrange
        workerRepository.save(new Worker(new CreateWorkerCommand("Ana", "Estilista", "https://paxtech.examplea.jpg", 1L)));
        workerRepository.save(new Worker(new CreateWorkerCommand("Luis", "Barbero", "https://paxtech.examplel.jpg", 2L)));

        // Act
        var result = service.handle(new GetAllWorkersQuery());

        // Assert
        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
    }
}
