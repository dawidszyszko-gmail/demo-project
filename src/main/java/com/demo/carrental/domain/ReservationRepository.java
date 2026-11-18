package com.demo.carrental.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {

    void save(Reservation reservation);

    List<Reservation> findByTypeAndPeriod(CarType carType, LocalDateTime start, LocalDateTime end);

    Optional<Reservation> findById(UUID id);
}

