package com.travel.bookingservice.client;

import com.travel.bookingservice.dto.HotelDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "hotel-service", url = "http://localhost:8083/hotels")
public interface HotelClient {

    @GetMapping("/{id}")
    HotelDTO getHotel(@PathVariable Long id);

    @PutMapping("/{id}/book")
    void bookHotel(@PathVariable Long id);
}
