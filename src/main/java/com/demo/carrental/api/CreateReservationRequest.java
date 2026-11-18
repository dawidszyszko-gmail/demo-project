package com.demo.carrental.api;

import com.demo.carrental.domain.CarType;
import com.demo.carrental.domain.ReservationRequest;

import java.time.LocalDateTime;

public record CreateReservationRequest(CarType carType, LocalDateTime startDateTime, Integer numberOfDays) {

    public ReservationRequest toDomainRequest() {
        if (carType == null) {
            throw new IllegalArgumentException("carType is required");
        }
        if (startDateTime == null) {
            throw new IllegalArgumentException("startDateTime is required");
        }
        if (numberOfDays == null) {
            throw new IllegalArgumentException("numberOfDays is required");
        }

        return new ReservationRequest(carType, startDateTime, numberOfDays);
    }
}

