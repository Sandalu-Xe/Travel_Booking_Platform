package com.travel.hotelservice.controller;

import com.travel.hotelservice.dto.HotelDTO;
import com.travel.hotelservice.entity.Hotel;
import com.travel.hotelservice.repository.HotelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelRepository hotelRepository;

    public HotelController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @PostMapping
    public ResponseEntity<HotelDTO> createHotel(@RequestBody Hotel hotel) {
        Hotel savedHotel = hotelRepository.save(hotel);
        return ResponseEntity.ok(mapToDTO(savedHotel));
    }

    @GetMapping
    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotel(@PathVariable Long id) {
        return hotelRepository.findById(id)
                .map(this::mapToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable Long id, @RequestBody Hotel hotelDetails) {
        return hotelRepository.findById(id)
                .map(hotel -> {
                    hotel.setName(hotelDetails.getName());
                    hotel.setLocation(hotelDetails.getLocation());
                    hotel.setPricePerNight(hotelDetails.getPricePerNight());
                    hotel.setAvailableRooms(hotelDetails.getAvailableRooms());
                    Hotel updatedHotel = hotelRepository.save(hotel);
                    return ResponseEntity.ok(mapToDTO(updatedHotel));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        return hotelRepository.findById(id)
                .map(hotel -> {
                    hotelRepository.delete(hotel);
                    return ResponseEntity.ok().<Void>build();
                })
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
                hotel.getAvailableRooms());
    }
}
