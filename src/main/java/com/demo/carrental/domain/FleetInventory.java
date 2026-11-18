package com.demo.carrental.domain;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class FleetInventory {

    private final Map<CarType, Integer> capacities;

    public FleetInventory(Map<CarType, Integer> capacities) {
        Objects.requireNonNull(capacities, "capacities cannot be null");

        EnumMap<CarType, Integer> copy = new EnumMap<>(CarType.class);
        for (CarType carType : CarType.values()) {
            int capacity = capacities.getOrDefault(carType, 0);
            if (capacity < 0) {
                throw new IllegalArgumentException("capacity cannot be negative for " + carType);
            }
            copy.put(carType, capacity);
        }

        this.capacities = Map.copyOf(copy);
    }

    public int capacityFor(CarType carType) {
        Objects.requireNonNull(carType, "carType cannot be null");
        return capacities.getOrDefault(carType, 0);
    }
}

