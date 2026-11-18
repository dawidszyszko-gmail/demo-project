package com.demo.carrental.api;

import com.demo.carrental.domain.Availability;
import com.demo.carrental.domain.CarRentalService;
import com.demo.carrental.domain.CarType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AvailabilityControllerTest {

    private MockMvc mockMvc;
    private CarRentalService carRentalService;

    @BeforeEach
    void setUp() {
        carRentalService = mock(CarRentalService.class);
        AvailabilityController controller = new AvailabilityController(carRentalService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void returnsAvailabilityForRequestedPeriod() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 11, 25, 10, 0);

        Availability availability = new Availability(
                CarType.SUV,
                start,
                start.plusDays(2),
                2,
                1,
                1
        );

        when(carRentalService.checkAvailability(CarType.SUV, start, 2)).thenReturn(availability);

        mockMvc.perform(get("/api/availability")
                        .param("carType", "SUV")
                        .param("startDateTime", "2026-11-25T10:00:00")
                        .param("numberOfDays", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carType").value("SUV"))
                .andExpect(jsonPath("$.capacity").value(2))
                .andExpect(jsonPath("$.reserved").value(1))
                .andExpect(jsonPath("$.available").value(1));
    }

    @Test
    void returnsBadRequestWhenInputIsInvalid() throws Exception {
        when(carRentalService.checkAvailability(CarType.VAN, LocalDateTime.of(2026, 11, 25, 10, 0), 0))
                .thenThrow(new IllegalArgumentException("numberOfDays must be greater than 0"));

        mockMvc.perform(get("/api/availability")
                        .param("carType", "VAN")
                        .param("startDateTime", "2026-11-25T10:00:00")
                        .param("numberOfDays", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }
}


