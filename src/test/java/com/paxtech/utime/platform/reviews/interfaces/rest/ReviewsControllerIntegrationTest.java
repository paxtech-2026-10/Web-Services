package com.paxtech.utime.platform.reviews.interfaces.rest;

import com.jayway.jsonpath.JsonPath;
import com.paxtech.utime.platform.services.infrastructure.persistence.jpa.repositories.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewsControllerIntegrationTest {

    private static final String BASE_URL = "/api/v1/services";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ServiceRepository serviceRepository;

    @BeforeEach
    void cleanDatabase() {
        serviceRepository.deleteAll();
    }

    // Verifica que el endpoint POST /api/v1/services crea un servicio correctamente y retorna 201 con el ServiceResource
    @Test
    @DisplayName("POST /api/v1/services: returns 201 with the created ServiceResource")
    void create_returns201_withServiceResource() throws Exception {
        String body = """
                {
                  "name": "Haircut",
                  "duration": 30,
                  "price": 5000,
                  "status": true,
                  "providerId": 1
                }
                """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Haircut"))
                .andExpect(jsonPath("$.duration").value(30))
                .andExpect(jsonPath("$.price").value(5000))
                .andExpect(jsonPath("$.providerId").value(1));
    }

    // Verifica que se pueden crear servicios con el mismo nombre siempre que pertenezcan a diferentes providers
    @Test
    @DisplayName("POST /api/v1/services: returns 201 when two services share the same name but belong to different providers")
    void create_returns201_whenSameNameForDifferentProviders() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Manicure","duration":45,"price":3000,"status":true,"providerId":1}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Manicure","duration":45,"price":3500,"status":true,"providerId":2}
                                """))
                .andExpect(status().isCreated());
    }

    // Verifica que se lanza IllegalArgumentException cuando se intenta crear un servicio duplicado para el mismo provider
    @Test
    @DisplayName("POST /api/v1/services: throws IllegalArgumentException when service name already exists for the same provider (no @ExceptionHandler → HTTP 500 in production)")
    void create_throwsIllegalArgument_whenDuplicateNameForSameProvider() throws Exception {
        String body = """
                {"name":"Coloring","duration":60,"price":8000,"status":true,"providerId":1}
                """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        assertThatThrownBy(() ->
                mockMvc.perform(post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                        .andReturn()
        ).hasRootCauseInstanceOf(IllegalArgumentException.class)
                .rootCause().hasMessageContaining("already exists");
    }

    // Verifica que el endpoint GET /api/v1/services retorna una lista vacía cuando no existen servicios
    @Test
    @DisplayName("GET /api/v1/services: returns 200 with an empty array when no services exist")
    void getAll_returns200_withEmptyArray_whenNoServicesExist() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // Verifica que el endpoint GET /api/v1/services retorna todos los servicios creados correctamente
    @Test
    @DisplayName("GET /api/v1/services: returns 200 with all persisted services")
    void getAll_returns200_withAllServices_afterCreation() throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"Haircut","duration":30,"price":5000,"status":true,"providerId":1}
                        """));
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"Manicure","duration":45,"price":3000,"status":true,"providerId":1}
                        """));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Haircut", "Manicure")));
    }

    // Verifica que el endpoint PUT /api/v1/services/{id} actualiza correctamente un servicio existente y retorna el recurso actualizado
    @Test
    @DisplayName("PUT /api/v1/services/{id}: returns 200 with the updated ServiceResource when the service exists")
    void update_returns200_withUpdatedResource_whenServiceExists() throws Exception {
        MvcResult createResult = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Old Name","duration":20,"price":2000,"status":true,"providerId":1}
                                """))
                .andReturn();

        long id = ((Number) JsonPath.read(
                createResult.getResponse().getContentAsString(), "$.id")).longValue();

        mockMvc.perform(put(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"New Name","duration":45,"price":9000,"providerId":1}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.duration").value(45))
                .andExpect(jsonPath("$.price").value(9000));
    }

    // Verifica que se lanza IllegalArgumentException cuando se intenta actualizar un servicio que no existe
    @Test
    @DisplayName("PUT /api/v1/services/{id}: throws IllegalArgumentException when the service does not exist (no @ExceptionHandler → HTTP 500 in production)")
    void update_throwsIllegalArgument_whenServiceDoesNotExist() {
        assertThatThrownBy(() ->
                mockMvc.perform(put(BASE_URL + "/99999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {"name":"Ghost","duration":30,"price":1000,"providerId":1}
                                """))
                        .andReturn()
        ).hasRootCauseInstanceOf(IllegalArgumentException.class)
                .rootCause().hasMessageContaining("does not exist");
    }

    // Verifica que el endpoint DELETE /api/v1/services/{id} elimina correctamente un servicio existente y lo remueve de la base de datos
    @Test
    @DisplayName("DELETE /api/v1/services/{id}: returns 200 and the service is gone from subsequent GET")
    void delete_returns200_andRemovesService_whenServiceExists() throws Exception {
        MvcResult createResult = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"ToDelete","duration":30,"price":5000,"status":true,"providerId":1}
                                """))
                .andReturn();

        long id = ((Number) JsonPath.read(
                createResult.getResponse().getContentAsString(), "$.id")).longValue();

        mockMvc.perform(delete(BASE_URL + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("successfully deleted")));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // Verifica que se lanza IllegalArgumentException cuando se intenta eliminar un servicio que no existe
    @Test
    @DisplayName("DELETE /api/v1/services/{id}: throws IllegalArgumentException when the service does not exist (no @ExceptionHandler → HTTP 500 in production)")
    void delete_throwsIllegalArgument_whenServiceDoesNotExist() {
        assertThatThrownBy(() ->
                mockMvc.perform(delete(BASE_URL + "/99999")).andReturn()
        ).hasRootCauseInstanceOf(IllegalArgumentException.class)
                .rootCause().hasMessageContaining("does not exist");
    }
}