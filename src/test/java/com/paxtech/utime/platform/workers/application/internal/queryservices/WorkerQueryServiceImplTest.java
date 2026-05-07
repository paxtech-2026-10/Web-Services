package com.paxtech.utime.platform.workers.application.internal.queryservices;

import com.paxtech.utime.platform.workers.domain.model.aggregates.Worker;
import com.paxtech.utime.platform.workers.domain.model.commands.CreateWorkerCommand;
import com.paxtech.utime.platform.workers.domain.model.queries.GetAllWorkersQuery;
import com.paxtech.utime.platform.workers.domain.model.queries.GetWorkerByIdQuery;
import com.paxtech.utime.platform.workers.domain.model.queries.GetWorkersBySalonIdQuery;
import com.paxtech.utime.platform.workers.domain.model.valueobjects.ProviderId;
import com.paxtech.utime.platform.workers.infrastructure.persistence.jpa.repositories.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkerQueryServiceImplTest {

    @Mock
    WorkerRepository workerRepository;

    @InjectMocks
    WorkerQueryServiceImpl workerQueryService;

    @Test
    void getBySalonIdTest() {
        // Arrange
        ProviderId pid = new ProviderId(3L);
        Worker w = new Worker(new CreateWorkerCommand("A", "B", "https://paxtech.examplex.jpg", 3L));
        List<Worker> expected = List.of(w);
        GetWorkersBySalonIdQuery query = new GetWorkersBySalonIdQuery(pid);
        when(workerRepository.findAllByProviderId(pid)).thenReturn(expected);

        // Act
        List<Worker> result = workerQueryService.handle(query);

        // Assert
        assertThat(result).isSameAs(expected);
        verify(workerRepository).findAllByProviderId(pid);
    }

    @Test
    void getByIdTest() {
        // Arrange
        Worker w = new Worker(new CreateWorkerCommand("A", "B", "https://paxtech.examplex.jpg", 1L));
        GetWorkerByIdQuery query = new GetWorkerByIdQuery(7L);
        when(workerRepository.findById(7L)).thenReturn(Optional.of(w));

        // Act
        Optional<Worker> result = workerQueryService.handle(query);

        // Assert
        assertThat(result).containsSame(w);
        verify(workerRepository).findById(7L);
    }

    @Test
    void getAllWorkersTest() {
        // Arrange
        Worker w = new Worker(new CreateWorkerCommand("A", "B", "https://paxtech.examplex.jpg", 1L));
        List<Worker> expected = List.of(w);
        GetAllWorkersQuery query = new GetAllWorkersQuery();
        when(workerRepository.findAll()).thenReturn(expected);

        // Act
        List<Worker> result = workerQueryService.handle(query);

        // Assert
        assertThat(result).isSameAs(expected);
        verify(workerRepository).findAll();
    }
}
