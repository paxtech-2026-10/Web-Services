package com.paxtech.utime.platform.reservations.interfaces.rest;

import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.ReservationRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link ReservationController}.
 *
 * Scope: HTTP → Spring Security → Controller → Service → Repository → H2 (in-memory).
 * No mocks are used — all application beans are real.
 *
 * Security: /api/v1/reservationsDetails/** is permitAll() in
 * WebSecurityConfiguration, so no Authorization header is required.
 *
 * Note on unhandled exceptions: the ReservationController forwards
 * IllegalArgumentException from {@code ReservationCommandServiceImpl}
 * (invalid command, missing id) without a {@code @ExceptionHandler} or global
 * {@code @ControllerAdvice}. In a real servlet container this becomes HTTP 500;
 * MockMvc re-throws the exception. The negative tests below assert this
 * exception-propagation contract explicitly.
 *
 * The {@code CreateReservation} command path does NOT verify foreign-key
 * existence against the Profiles, Services, Workers or TimeSlots bounded
 * contexts — only that every supplied id is a positive {@code Long}. The
 * tests therefore use synthetic ids and focus on the Reservations bounded
 * context end-to-end pipeline.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerIT {

    private static final String BASE_URL = "/api/v1/reservationsDetails";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void cleanDatabase() {
        reservationRepository.deleteAll();
    }

    // =========================================================================
    // POST /api/v1/reservationsDetails
    // =========================================================================

    @Test
    @DisplayName("POST /api/v1/reservationsDetails: returns 201 with the created ReservationResource")
    void create_returns201_withReservationResource() throws Exception {
        String body = """
                {
                  "clientId": 1,
                  "providerId": 2,
                  "serviceId": 3,
                  "timeSlotId": 4,
                  "workerId": 5
                }
                """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.providerId").value(2))
                .andExpect(jsonPath("$.serviceId").value(3))
                .andExpect(jsonPath("$.timeSlotId").value(4))
                .andExpect(jsonPath("$.workerId").value(5));
    }

    @Test
    @DisplayName("POST /api/v1/reservationsDetails: invalid id (≤ 0) propagates IllegalArgumentException (HTTP 500 in prod)")
    void create_withInvalidId_propagatesException() {
        String body = """
                {
                  "clientId": 0,
                  "providerId": 2,
                  "serviceId": 3,
                  "timeSlotId": 4,
                  "workerId": 5
                }
                """;

        assertThatThrownBy(() ->
                mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
        ).hasCauseInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("Invalid reservation command");
    }

    @Test
    @DisplayName("POST /api/v1/reservationsDetails: null id propagates IllegalArgumentException")
    void create_withNullId_propagatesException() {
        String body = """
                {
                  "clientId": null,
                  "providerId": 2,
                  "serviceId": 3,
                  "timeSlotId": 4,
                  "workerId": 5
                }
                """;

        assertThatThrownBy(() ->
                mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
        ).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    // =========================================================================
    // GET /api/v1/reservationsDetails/{id}
    // =========================================================================

    @Test
    @DisplayName("GET /api/v1/reservationsDetails/{id}: returns 404 when the reservation does not exist")
    void getById_returns404_whenMissing() throws Exception {
        mockMvc.perform(get(BASE_URL + "/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/reservationsDetails/{id}: returns 200 with the reservation after create")
    void getById_returnsReservation_afterCreate() throws Exception {
        MvcResult created = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"clientId":7,"providerId":8,"serviceId":9,"timeSlotId":10,"workerId":11}
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String id = com.jayway.jsonpath.JsonPath.read(created.getResponse().getContentAsString(), "$.id").toString();

        mockMvc.perform(get(BASE_URL + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Integer.parseInt(id)))
                .andExpect(jsonPath("$.clientId").value(7))
                .andExpect(jsonPath("$.providerId").value(8))
                .andExpect(jsonPath("$.serviceId").value(9))
                .andExpect(jsonPath("$.timeSlotId").value(10))
                .andExpect(jsonPath("$.workerId").value(11));
    }

    // =========================================================================
    // GET /api/v1/reservationsDetails (all)
    // =========================================================================

    @Test
    @DisplayName("GET /api/v1/reservationsDetails: returns 404 when there are no reservations")
    void getAll_returns404_whenEmpty() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/reservationsDetails: returns the list after creating two reservations")
    void getAll_returnsList_afterTwoCreates() throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"clientId":1,"providerId":1,"serviceId":1,"timeSlotId":1,"workerId":1}
                                """))
                .andExpect(status().isCreated());
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"clientId":2,"providerId":2,"serviceId":2,"timeSlotId":2,"workerId":2}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // =========================================================================
    // DELETE /api/v1/reservationsDetails/{id}
    // =========================================================================

    @Test
    @DisplayName("DELETE /api/v1/reservationsDetails/{id}: removes the reservation; GET afterwards returns 404")
    void delete_removesReservation() throws Exception {
        MvcResult created = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"clientId":1,"providerId":1,"serviceId":1,"timeSlotId":1,"workerId":1}
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String id = com.jayway.jsonpath.JsonPath.read(created.getResponse().getContentAsString(), "$.id").toString();

        mockMvc.perform(delete(BASE_URL + "/" + id))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get(BASE_URL + "/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/reservationsDetails/{id}: non-existing id is handled by the controller's try/catch")
    void delete_nonExisting_isHandled() throws Exception {
        // The controller wraps the delete command in a try/catch and returns
        // an HTTP response instead of bubbling the IllegalArgumentException.
        mockMvc.perform(delete(BASE_URL + "/99999"))
                .andExpect(status().is4xxClientError());
    }
}
