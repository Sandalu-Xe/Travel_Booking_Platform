package com.travel.flightservice.controller;

import com.travel.flightservice.dto.FlightDTO;
import com.travel.flightservice.entity.Flight;
import com.travel.flightservice.repository.FlightRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightRepository flightRepository;

    public FlightController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @PostMapping
    public ResponseEntity<FlightDTO> createFlight(@RequestBody Flight flight) {
        Flight savedFlight = flightRepository.save(flight);
        return ResponseEntity.ok(mapToDTO(savedFlight));
    }

    @GetMapping
    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlight(@PathVariable Long id) {
        return flightRepository.findById(id)
                .map(this::mapToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@PathVariable Long id, @RequestBody Flight flightDetails) {
        return flightRepository.findById(id)
                .map(flight -> {
                    flight.setFlightNumber(flightDetails.getFlightNumber());
                    flight.setOrigin(flightDetails.getOrigin());
                    flight.setDestination(flightDetails.getDestination());
                    flight.setFlightDate(flightDetails.getFlightDate());
                    flight.setPrice(flightDetails.getPrice());
                    flight.setAvailableSeats(flightDetails.getAvailableSeats());
                    Flight updatedFlight = flightRepository.save(flight);
                    return ResponseEntity.ok(mapToDTO(updatedFlight));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        return flightRepository.findById(id)
                .map(flight -> {
                    flightRepository.delete(flight);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/book")
    public ResponseEntity<Void> bookFlight(@PathVariable Long id) {
        return flightRepository.findById(id)
                .map(flight -> {
                    if (flight.getAvailableSeats() > 0) {
                        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
                        flightRepository.save(flight);
                        return ResponseEntity.ok().<Void>build();
                    } else {
                        return ResponseEntity.badRequest().<Void>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private FlightDTO mapToDTO(Flight flight) {
        return new FlightDTO(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getFlightDate(),
                flight.getPrice(),
                flight.getAvailableSeats());
    }
}
