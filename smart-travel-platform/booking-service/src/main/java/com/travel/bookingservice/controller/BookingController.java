package com.travel.bookingservice.controller;

import com.travel.bookingservice.entity.Booking;
import com.travel.bookingservice.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(
                request.getUserId(),
                request.getFlightId(),
                request.getHotelId(),
                request.getTravelDate()
        );
        return ResponseEntity.ok(booking);
    }

    public static class BookingRequest {
        private Long userId;
        private Long flightId;
        private Long hotelId;
        private LocalDate travelDate;

        public BookingRequest() {}

        public BookingRequest(Long userId, Long flightId, Long hotelId, LocalDate travelDate) {
            this.userId = userId;
            this.flightId = flightId;
            this.hotelId = hotelId;
            this.travelDate = travelDate;
        }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getFlightId() { return flightId; }
        public void setFlightId(Long flightId) { this.flightId = flightId; }
        public Long getHotelId() { return hotelId; }
        public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
        public LocalDate getTravelDate() { return travelDate; }
        public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }
    }
}
