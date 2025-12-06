package com.travel.flightservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class FlightServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightServiceApplication.class, args);
	}

    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner init(com.travel.flightservice.repository.FlightRepository repository) {
        return args -> {
            repository.save(new com.travel.flightservice.entity.Flight(null, "FL123", "NYC", "LAX", java.time.LocalDate.of(2025, 1, 10), 500.0, 100));
            repository.save(new com.travel.flightservice.entity.Flight(null, "FL456", "LAX", "NYC", java.time.LocalDate.of(2025, 1, 15), 450.0, 50));
        };
    }

}
