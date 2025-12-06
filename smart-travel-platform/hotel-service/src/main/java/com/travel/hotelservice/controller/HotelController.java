package com.travel.hotelservice.controller;

import com.travel.hotelservice.dto.HotelDTO;
import com.travel.hotelservice.entity.Hotel;
import com.travel.hotelservice.repository.HotelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelRepository hotelRepository;

    public HotelController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotel(@PathVariable Long id) {
        return hotelRepository.findById(id)
                .map(this::mapToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/book")
    public ResponseEntity<Void> bookHotel(@PathVariable Long id) {
        return hotelRepository.findById(id)
                .map(hotel -> {
                    if (hotel.getAvailableRooms() > 0) {
                        hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
                        hotelRepository.save(hotel);
                        return ResponseEntity.ok().<Void>build();
                    } else {
                        return ResponseEntity.badRequest().<Void>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private HotelDTO mapToDTO(Hotel hotel) {
        return new HotelDTO(
                hotel.getId(),
                hotel.getName(),
                hotel.getLocation(),
                hotel.getPricePerNight(),
                hotel.getAvailableRooms()
        );
    }
}
