package com.paxtech.utime.platform.workers.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.domain.services.ObjectStorageService;
import com.paxtech.utime.platform.profiles.interfaces.acl.ProviderContextFacade;
import com.paxtech.utime.platform.workers.domain.model.aggregates.Worker;
import com.paxtech.utime.platform.workers.domain.model.commands.CreateWorkerCommand;
import com.paxtech.utime.platform.workers.infrastructure.persistence.jpa.repositories.WorkerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WorkersControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WorkerRepository workerRepository;

    @MockBean
    private ProviderContextFacade providerContextFacade;

    @MockBean
    private ObjectStorageService objectStorageService;

    @BeforeEach
    void setUp() {
        workerRepository.deleteAll();
        when(providerContextFacade.fetchProviderById(10L)).thenReturn(Optional.of(mock(Provider.class)));
        when(providerContextFacade.fetchProviderById(20L)).thenReturn(Optional.of(mock(Provider.class)));
    }

    @Test
    @WithMockUser
    void createWorkerEndpointIntegrationTest() throws Exception {
        // Arrange
        String body = """
                {
                  "name": "Ana",
                  "specialization": "Estilista",
                  "photoUrl": "https://paxtech.examplea.jpg",
                  "providerId": 10
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/v1/workers")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ana"))
                .andExpect(jsonPath("$.providerId").value(10));
    }

    @Test
    @WithMockUser
    void getWorkerByIdEndpointIntegrationTest() throws Exception {
        // Arrange
        Worker worker = workerRepository.save(
                new Worker(new CreateWorkerCommand("Luis", "Barbero", "https://paxtech.examplel.jpg", 10L))
        );

        // Act & Assert
        mockMvc.perform(get("/api/v1/workers/{workerId}", worker.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(worker.getId()))
                .andExpect(jsonPath("$.name").value("Luis"));
    }

    @Test
    @WithMockUser
    void updateWorkerEndpointIntegrationTest() throws Exception {
        // Arrange
        Worker worker = workerRepository.save(
                new Worker(new CreateWorkerCommand("Mario", "Barbero", "https://paxtech.examplem.jpg", 10L))
        );
        String body = objectMapper.writeValueAsString(
                new com.paxtech.utime.platform.workers.interfaces.rest.resources.UpdateWorkerResource(
                        "Mario Updated",
                        "Colorista",
                        "https://paxtech.examplenew.jpg",
                        20L
                )
        );

        // Act & Assert
        mockMvc.perform(put("/api/v1/workers/{workerId}", worker.getId())
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mario Updated"))
                .andExpect(jsonPath("$.providerId").value(20));
    }

    @Test
    @WithMockUser
    void deleteWorkerEndpointIntegrationTest() throws Exception {
        // Arrange
        Worker worker = workerRepository.save(
                new Worker(new CreateWorkerCommand("Carlos", "Barbero", "https://paxtech.examplec.jpg", 10L))
        );

        // Act & Assert
        mockMvc.perform(delete("/api/v1/workers/{workerId}", worker.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void workersEndpointsRequireAuthenticationIntegrationTest() throws Exception {
        // Arrange + Act + Assert
        mockMvc.perform(get("/api/v1/workers"))
                .andExpect(status().isUnauthorized());
    }
}
