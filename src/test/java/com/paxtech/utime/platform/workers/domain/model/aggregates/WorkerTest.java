package com.paxtech.utime.platform.workers.domain.model.aggregates;

import com.paxtech.utime.platform.workers.domain.model.commands.CreateWorkerCommand;
import com.paxtech.utime.platform.workers.domain.model.valueobjects.ProviderId;
import com.paxtech.utime.platform.workers.domain.model.valueobjects.WorkerName;
import com.paxtech.utime.platform.workers.domain.model.valueobjects.WorkerPhotoUrl;
import com.paxtech.utime.platform.workers.domain.model.valueobjects.WorkerSpecialization;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class WorkerTest {

    @Test
    void createWorkerFromValueObjectsTest() {
        // Arrange
        Worker worker = new Worker(
                new WorkerName("Ana"),
                new WorkerSpecialization("Estilista"),
                new WorkerPhotoUrl("https://cdn.example/a.jpg"),
                new ProviderId(42L)
        );

        // Act
        String name = worker.getName();
        String specialization = worker.getSpecialization();
        String photoUrl = worker.getPhotoUrl();
        Long salonId = worker.getSalonId();

        // Assert
        assertThat(name).isEqualTo("Ana");
        assertThat(specialization).isEqualTo("Estilista");
        assertThat(photoUrl).isEqualTo("https://cdn.example/a.jpg");
        assertThat(salonId).isEqualTo(42L);
    }

    @Test
    void createWorkerFromCommandTest() {
        // Arrange
        CreateWorkerCommand command = new CreateWorkerCommand(
                "Luis",
                "Barbero",
                "https://cdn.example/l.png",
                99L
        );

        // Act
        Worker worker = new Worker(command);

        // Assert
        assertThat(worker.getName()).isEqualTo("Luis");
        assertThat(worker.getSpecialization()).isEqualTo("Barbero");
        assertThat(worker.getPhotoUrl()).isEqualTo("https://cdn.example/l.png");
        assertThat(worker.getSalonId()).isEqualTo(99L);
    }

    @Test
    void updateInformationTest() {
        // Arrange
        Worker worker = new Worker(
                new WorkerName("Ana"),
                new WorkerSpecialization("Estilista"),
                new WorkerPhotoUrl("https://cdn.example/old.jpg"),
                new ProviderId(1L)
        );

        // Act
        Worker returned = worker.updateInformation(
                "Nuevo",
                "Barbero",
                "https://cdn.example/new.jpg",
                7L
        );

        // Assert
        assertThat(returned).isSameAs(worker);
        assertThat(worker.getName()).isEqualTo("Nuevo");
        assertThat(worker.getSpecialization()).isEqualTo("Barbero");
        assertThat(worker.getPhotoUrl()).isEqualTo("https://cdn.example/new.jpg");
        assertThat(worker.getSalonId()).isEqualTo(7L);
    }

    @Test
    void invalidCreateWorkerCommandTest() {
        // Arrange
        CreateWorkerCommand bad = new CreateWorkerCommand(
                "",
                "Rol",
                "https://cdn.example/x.jpg",
                1L
        );

        // Act & Assert
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Worker(bad))
                .withMessageContaining("Name");
    }
}
