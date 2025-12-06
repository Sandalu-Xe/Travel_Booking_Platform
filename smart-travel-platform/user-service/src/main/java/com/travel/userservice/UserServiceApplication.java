package com.travel.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner init(com.travel.userservice.repository.UserRepository repository) {
        return args -> {
            repository.save(new com.travel.userservice.entity.User(null, "John Doe", "john@example.com"));
            repository.save(new com.travel.userservice.entity.User(null, "Jane Smith", "jane@example.com"));
        };
    }

}
