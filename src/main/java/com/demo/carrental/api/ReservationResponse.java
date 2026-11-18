package com.demo.carrental.api;

import com.demo.carrental.domain.CarType;
import com.demo.carrental.domain.Reservation;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(UUID id, CarType carType, LocalDateTime start, LocalDateTime end) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.carType(),
                reservation.start(),
                reservation.end()
        );
    }
}

