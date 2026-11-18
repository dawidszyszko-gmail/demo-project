package com.demo.carrental.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public record Availability(
        CarType carType,
        LocalDateTime start,
        LocalDateTime end,
        int capacity,
        int reserved,
        int available
) {
    public Availability {
        Objects.requireNonNull(carType, "carType cannot be null");
        Objects.requireNonNull(start, "start cannot be null");
        Objects.requireNonNull(end, "end cannot be null");

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("end must be after start");
        }
        if (capacity < 0 || reserved < 0 || available < 0) {
            throw new IllegalArgumentException("capacity and counts cannot be negative");
        }
        if (reserved > capacity) {
            throw new IllegalArgumentException("reserved cannot exceed capacity");
        }
        if (available != capacity - reserved) {
            throw new IllegalArgumentException("available must equal capacity - reserved");
        }
    }
}

