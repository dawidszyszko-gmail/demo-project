package com.demo.carrental.config;

import com.demo.carrental.domain.CarRentalService;
import com.demo.carrental.domain.CarType;
import com.demo.carrental.domain.FleetInventory;
import com.demo.carrental.domain.InMemoryReservationRepository;
import com.demo.carrental.domain.ReservationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CarRentalConfiguration {

    @Bean
    public FleetInventory fleetInventory(
            @Value("${carrental.fleet.sedan:2}") int sedanCapacity,
            @Value("${carrental.fleet.suv:2}") int suvCapacity,
            @Value("${carrental.fleet.van:1}") int vanCapacity
    ) {
        return new FleetInventory(Map.of(
                CarType.SEDAN, sedanCapacity,
                CarType.SUV, suvCapacity,
                CarType.VAN, vanCapacity
        ));
    }

    @Bean
    public ReservationRepository reservationRepository() {
        return new InMemoryReservationRepository();
    }

    @Bean
    public CarRentalService carRentalService(FleetInventory fleetInventory, ReservationRepository reservationRepository) {
        return new CarRentalService(fleetInventory, reservationRepository);
    }
}

