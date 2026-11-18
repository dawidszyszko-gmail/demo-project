package com.demo.carrental.api;

import com.demo.carrental.domain.CarRentalService;
import com.demo.carrental.domain.CarType;
import com.demo.carrental.domain.NoCarsAvailableException;
import com.demo.carrental.domain.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest {

    private MockMvc mockMvc;
    private CarRentalService carRentalService;

    @BeforeEach
    void setUp() {
        carRentalService = mock(CarRentalService.class);
        ReservationController controller = new ReservationController(carRentalService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createsReservationAndReturnsCreated() throws Exception {
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Reservation reservation = new Reservation(
                id,
                CarType.SUV,
                LocalDateTime.of(2026, 11, 25, 10, 0),
                LocalDateTime.of(2026, 11, 27, 10, 0)
        );

        when(carRentalService.reserve(any())).thenReturn(reservation);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "carType": "SUV",
                                  "startDateTime": "2026-11-25T10:00:00",
                                  "numberOfDays": 2
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/reservations/11111111-1111-1111-1111-111111111111"))
                .andExpect(jsonPath("$.id").value("11111111-1111-1111-1111-111111111111"))
                .andExpect(jsonPath("$.carType").value("SUV"));
    }

    @Test
    void returnsReservationByIdWhenFound() throws Exception {
        UUID id = UUID.fromString("22222222-2222-2222-2222-222222222222");
        Reservation reservation = new Reservation(
                id,
                CarType.SEDAN,
                LocalDateTime.of(2026, 11, 26, 9, 0),
                LocalDateTime.of(2026, 11, 28, 9, 0)
        );

        when(carRentalService.findReservation(eq(id))).thenReturn(Optional.of(reservation));

        mockMvc.perform(get("/api/reservations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("22222222-2222-2222-2222-222222222222"))
                .andExpect(jsonPath("$.carType").value("SEDAN"));
    }

    @Test
    void returnsNotFoundWhenReservationByIdDoesNotExist() throws Exception {
        UUID id = UUID.fromString("33333333-3333-3333-3333-333333333333");
        when(carRentalService.findReservation(eq(id))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservations/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnsConflictWhenNoCarsAreAvailable() throws Exception {
        when(carRentalService.reserve(any())).thenThrow(new NoCarsAvailableException(CarType.VAN));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "carType": "VAN",
                                  "startDateTime": "2026-11-25T10:00:00",
                                  "numberOfDays": 1
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("NO_CARS_AVAILABLE"));
    }

    @Test
    void returnsBadRequestForInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "carType": null,
                                  "startDateTime": "2026-11-25T10:00:00",
                                  "numberOfDays": 2
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }
}



