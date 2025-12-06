package com.travel.hotelservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class HotelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelServiceApplication.class, args);
	}

    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner init(com.travel.hotelservice.repository.HotelRepository repository) {
        return args -> {
            repository.save(new com.travel.hotelservice.entity.Hotel(null, "Grand Hotel", "NYC", 200.0, 50));
            repository.save(new com.travel.hotelservice.entity.Hotel(null, "Beach Resort", "LAX", 300.0, 20));
        };
    }

}
