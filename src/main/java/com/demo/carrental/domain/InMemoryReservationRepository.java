package com.demo.carrental.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class InMemoryReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public synchronized void save(Reservation reservation) {
        reservations.add(Objects.requireNonNull(reservation, "reservation cannot be null"));
    }

    @Override
    public synchronized List<Reservation> findByTypeAndPeriod(CarType carType, LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(carType, "carType cannot be null");
        Objects.requireNonNull(start, "start cannot be null");
        Objects.requireNonNull(end, "end cannot be null");

        return reservations.stream()
                .filter(reservation -> reservation.carType() == carType)
                .filter(reservation -> reservation.overlaps(start, end))
                .toList();
    }

    @Override
    public synchronized Optional<Reservation> findById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");

        return reservations.stream()
                .filter(reservation -> reservation.id().equals(id))
                .findFirst();
    }
}

