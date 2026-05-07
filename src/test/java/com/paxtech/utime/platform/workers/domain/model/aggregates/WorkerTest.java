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
                new WorkerPhotoUrl("https://paxtech.examplea.jpg"),
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
        assertThat(photoUrl).isEqualTo("https://paxtech.examplea.jpg");
        assertThat(salonId).isEqualTo(42L);
    }

    @Test
    void createWorkerFromCommandTest() {
        // Arrange
        CreateWorkerCommand command = new CreateWorkerCommand(
                "Luis",
                "Barbero",
                "https://paxtech.examplel.png",
                99L
        );

        // Act
        Worker worker = new Worker(command);

        // Assert
        assertThat(worker.getName()).isEqualTo("Luis");
        assertThat(worker.getSpecialization()).isEqualTo("Barbero");
        assertThat(worker.getPhotoUrl()).isEqualTo("https://paxtech.examplel.png");
        assertThat(worker.getSalonId()).isEqualTo(99L);
    }

    @Test
    void updateInformationTest() {
        // Arrange
        Worker worker = new Worker(
                new WorkerName("Ana"),
                new WorkerSpecialization("Estilista"),
                new WorkerPhotoUrl("https://paxtech.exampleold.jpg"),
                new ProviderId(1L)
        );

        // Act
        Worker returned = worker.updateInformation(
                "Nuevo",
                "Barbero",
                "https://paxtech.examplenew.jpg",
                7L
        );

        // Assert
        assertThat(returned).isSameAs(worker);
        assertThat(worker.getName()).isEqualTo("Nuevo");
        assertThat(worker.getSpecialization()).isEqualTo("Barbero");
        assertThat(worker.getPhotoUrl()).isEqualTo("https://paxtech.examplenew.jpg");
        assertThat(worker.getSalonId()).isEqualTo(7L);
    }

    @Test
    void invalidCreateWorkerCommandTest() {
        // Arrange
        CreateWorkerCommand bad = new CreateWorkerCommand(
                "",
                "Rol",
                "https://paxtech.examplex.jpg",
                1L
        );

        // Act & Assert
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Worker(bad))
                .withMessageContaining("Name");
    }
}
