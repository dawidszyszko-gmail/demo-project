package com.demo.carrental.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class CarRentalService {

    private final FleetInventory fleetInventory;
    private final ReservationRepository reservationRepository;

    public CarRentalService(FleetInventory fleetInventory, ReservationRepository reservationRepository) {
        this.fleetInventory = Objects.requireNonNull(fleetInventory, "fleetInventory cannot be null");
        this.reservationRepository = Objects.requireNonNull(reservationRepository, "reservationRepository cannot be null");
    }

    public Reservation reserve(ReservationRequest request) {
        Objects.requireNonNull(request, "request cannot be null");

        LocalDateTime start = request.startDateTime();
        LocalDateTime end = start.plusDays(request.numberOfDays());

        int capacity = fleetInventory.capacityFor(request.carType());
        int occupiedCars = reservationRepository.findByTypeAndPeriod(request.carType(), start, end).size();

        if (occupiedCars >= capacity) {
            throw new NoCarsAvailableException(request.carType());
        }

        Reservation reservation = new Reservation(UUID.randomUUID(), request.carType(), start, end);
        reservationRepository.save(reservation);
        return reservation;
    }

    public Optional<Reservation> findReservation(UUID reservationId) {
        Objects.requireNonNull(reservationId, "reservationId cannot be null");
        return reservationRepository.findById(reservationId);
    }

    public Availability checkAvailability(CarType carType, LocalDateTime startDateTime, int numberOfDays) {
        Objects.requireNonNull(carType, "carType cannot be null");
        Objects.requireNonNull(startDateTime, "startDateTime cannot be null");
        if (numberOfDays <= 0) {
            throw new IllegalArgumentException("numberOfDays must be greater than 0");
        }

        LocalDateTime endDateTime = startDateTime.plusDays(numberOfDays);
        int capacity = fleetInventory.capacityFor(carType);
        int reserved = reservationRepository.findByTypeAndPeriod(carType, startDateTime, endDateTime).size();

        return new Availability(carType, startDateTime, endDateTime, capacity, reserved, capacity - reserved);
    }
}

