package com.demo.carrental.api;

import com.demo.carrental.domain.Availability;
import com.demo.carrental.domain.CarType;

import java.time.LocalDateTime;

public record AvailabilityResponse(
        CarType carType,
        LocalDateTime start,
        LocalDateTime end,
        int capacity,
        int reserved,
        int available
) {
    public static AvailabilityResponse from(Availability availability) {
        return new AvailabilityResponse(
                availability.carType(),
                availability.start(),
                availability.end(),
                availability.capacity(),
                availability.reserved(),
                availability.available()
        );
    }
}

