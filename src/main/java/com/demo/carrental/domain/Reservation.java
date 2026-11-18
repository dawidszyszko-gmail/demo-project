package com.demo.carrental.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Reservation {

    private final UUID id;
    private final CarType carType;
    private final LocalDateTime start;
    private final LocalDateTime end;

    public Reservation(UUID id, CarType carType, LocalDateTime start, LocalDateTime end) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.carType = Objects.requireNonNull(carType, "carType cannot be null");
        this.start = Objects.requireNonNull(start, "start cannot be null");
        this.end = Objects.requireNonNull(end, "end cannot be null");

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("end must be after start");
        }
    }

    public UUID id() {
        return id;
    }

    public CarType carType() {
        return carType;
    }

    public LocalDateTime start() {
        return start;
    }

    public LocalDateTime end() {
        return end;
    }

    public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        Objects.requireNonNull(otherStart, "otherStart cannot be null");
        Objects.requireNonNull(otherEnd, "otherEnd cannot be null");

        return start.isBefore(otherEnd) && otherStart.isBefore(end);
    }
}

