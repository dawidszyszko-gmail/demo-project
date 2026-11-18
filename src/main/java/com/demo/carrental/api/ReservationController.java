package com.demo.carrental.api;

import com.demo.carrental.domain.CarRentalService;
import com.demo.carrental.domain.NoCarsAvailableException;
import com.demo.carrental.domain.Reservation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final CarRentalService carRentalService;

    public ReservationController(CarRentalService carRentalService) {
        this.carRentalService = carRentalService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody CreateReservationRequest request) {
        Reservation reservation = carRentalService.reserve(request.toDomainRequest());

        return ResponseEntity
                .created(URI.create("/api/reservations/" + reservation.id()))
                .body(ReservationResponse.from(reservation));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable UUID id) {
        return carRentalService.findReservation(id)
                .map(ReservationResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(NoCarsAvailableException.class)
    public ResponseEntity<ErrorResponse> handleNoCarsAvailable(NoCarsAvailableException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("NO_CARS_AVAILABLE", exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("INVALID_REQUEST", exception.getMessage()));
    }
}


