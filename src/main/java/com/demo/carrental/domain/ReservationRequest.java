package com.demo.carrental.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public record ReservationRequest(CarType carType, LocalDateTime startDateTime, int numberOfDays) {

    public ReservationRequest {
        Objects.requireNonNull(carType, "carType cannot be null");
        Objects.requireNonNull(startDateTime, "startDateTime cannot be null");

        if (numberOfDays <= 0) {
            throw new IllegalArgumentException("numberOfDays must be greater than 0");
        }
    }
}

