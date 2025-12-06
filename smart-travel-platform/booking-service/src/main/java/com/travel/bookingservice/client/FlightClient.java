package com.travel.bookingservice.client;

import com.travel.bookingservice.dto.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "flight-service", url = "http://localhost:8082/flights")
public interface FlightClient {

    @GetMapping("/{id}")
    FlightDTO getFlight(@PathVariable Long id);

    @PutMapping("/{id}/book")
    void bookFlight(@PathVariable Long id);
}
