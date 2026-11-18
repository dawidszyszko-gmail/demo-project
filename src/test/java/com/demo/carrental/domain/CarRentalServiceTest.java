package com.demo.carrental.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarRentalServiceTest {

    @Test
    void reservesCarWhenCapacityIsAvailable() {
        CarRentalService service = new CarRentalService(
                new FleetInventory(Map.of(CarType.SEDAN, 1, CarType.SUV, 1, CarType.VAN, 1)),
                new InMemoryReservationRepository()
        );

        Reservation reservation = service.reserve(new ReservationRequest(
                CarType.SEDAN,
                LocalDateTime.of(2026, 11, 20, 10, 0),
                2
        ));

        assertNotNull(reservation.id());
        assertEquals(CarType.SEDAN, reservation.carType());
        assertEquals(LocalDateTime.of(2026, 11, 20, 10, 0), reservation.start());
        assertEquals(LocalDateTime.of(2026, 11, 22, 10, 0), reservation.end());
    }

    @Test
    void rejectsReservationWhenNoCarsOfTypeAreAvailable() {
        CarRentalService service = new CarRentalService(
                new FleetInventory(Map.of(CarType.SEDAN, 1, CarType.SUV, 1, CarType.VAN, 1)),
                new InMemoryReservationRepository()
        );

        ReservationRequest first = new ReservationRequest(
                CarType.SUV,
                LocalDateTime.of(2026, 11, 20, 10, 0),
                3
        );
        ReservationRequest second = new ReservationRequest(
                CarType.SUV,
                LocalDateTime.of(2026, 11, 21, 9, 0),
                2
        );

        service.reserve(first);

        assertThrows(NoCarsAvailableException.class, () -> service.reserve(second));
    }

    @Test
    void allowsBackToBackReservationsWithoutOverlap() {
        CarRentalService service = new CarRentalService(
                new FleetInventory(Map.of(CarType.SEDAN, 1, CarType.SUV, 1, CarType.VAN, 1)),
                new InMemoryReservationRepository()
        );

        service.reserve(new ReservationRequest(
                CarType.VAN,
                LocalDateTime.of(2026, 11, 20, 10, 0),
                2
        ));

        Reservation nextReservation = service.reserve(new ReservationRequest(
                CarType.VAN,
                LocalDateTime.of(2026, 11, 22, 10, 0),
                1
        ));

        assertEquals(LocalDateTime.of(2026, 11, 22, 10, 0), nextReservation.start());
    }

    @Test
    void managesAvailabilityPerCarTypeIndependently() {
        CarRentalService service = new CarRentalService(
                new FleetInventory(Map.of(CarType.SEDAN, 1, CarType.SUV, 1, CarType.VAN, 1)),
                new InMemoryReservationRepository()
        );

        service.reserve(new ReservationRequest(
                CarType.SEDAN,
                LocalDateTime.of(2026, 11, 20, 10, 0),
                5
        ));

        Reservation suvReservation = service.reserve(new ReservationRequest(
                CarType.SUV,
                LocalDateTime.of(2026, 11, 21, 10, 0),
                2
        ));

        assertEquals(CarType.SUV, suvReservation.carType());
    }

    @Test
    void validatesNumberOfDaysInRequest() {
        LocalDateTime start = LocalDateTime.of(2026, 11, 20, 10, 0);

        assertThrows(IllegalArgumentException.class, () -> new ReservationRequest(
                CarType.SEDAN,
                start,
                0
        ));
    }

    @Test
    void findsReservationByIdWhenItExists() {
        CarRentalService service = new CarRentalService(
                new FleetInventory(Map.of(CarType.SEDAN, 1, CarType.SUV, 1, CarType.VAN, 1)),
                new InMemoryReservationRepository()
        );

        Reservation reservation = service.reserve(new ReservationRequest(
                CarType.SEDAN,
                LocalDateTime.of(2026, 11, 20, 10, 0),
                2
        ));

        assertTrue(service.findReservation(reservation.id()).isPresent());
    }

    @Test
    void returnsAvailabilityWithCapacityAndReservedCars() {
        CarRentalService service = new CarRentalService(
                new FleetInventory(Map.of(CarType.SEDAN, 2, CarType.SUV, 1, CarType.VAN, 1)),
                new InMemoryReservationRepository()
        );

        service.reserve(new ReservationRequest(
                CarType.SEDAN,
                LocalDateTime.of(2026, 11, 20, 10, 0),
                3
        ));

        Availability availability = service.checkAvailability(
                CarType.SEDAN,
                LocalDateTime.of(2026, 11, 21, 10, 0),
                1
        );

        assertEquals(2, availability.capacity());
        assertEquals(1, availability.reserved());
        assertEquals(1, availability.available());
    }
}


