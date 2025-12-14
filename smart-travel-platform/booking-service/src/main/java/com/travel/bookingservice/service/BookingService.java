package com.travel.bookingservice.service;

import com.travel.bookingservice.client.FlightClient;
import com.travel.bookingservice.client.HotelClient;
import com.travel.bookingservice.dto.FlightDTO;
import com.travel.bookingservice.dto.HotelDTO;
import com.travel.bookingservice.entity.Booking;
import com.travel.bookingservice.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Map;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final FlightClient flightClient;
    private final HotelClient hotelClient;
    private final WebClient.Builder webClientBuilder;

    public BookingService(BookingRepository bookingRepository, FlightClient flightClient, HotelClient hotelClient,
            WebClient.Builder webClientBuilder) {
        this.bookingRepository = bookingRepository;
        this.flightClient = flightClient;
        this.hotelClient = hotelClient;
        this.webClientBuilder = webClientBuilder;
    }

    public Booking createBooking(Long userId, Long flightId, Long hotelId, LocalDate travelDate) {
        log.info("Starting booking process for user {}", userId);

        // 1. Validate User
        try {
            webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8081/users/" + userId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("User not found or service unavailable");
        }

        // 2. Check Flight Availability
        FlightDTO flight = flightClient.getFlight(flightId);
        if (flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("Flight not available");
        }

        // 3. Check Hotel Availability
        HotelDTO hotel = hotelClient.getHotel(hotelId);
        if (hotel.getAvailableRooms() <= 0) {
            throw new RuntimeException("Hotel not available");
        }

        // 4. Calculate Total Cost
        Double totalCost = flight.getPrice() + hotel.getPricePerNight(); // Assuming 1 night for simplicity

        // 5. Store Booking as PENDING
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setFlightId(flightId);
        booking.setHotelId(hotelId);
        booking.setTravelDate(travelDate);
        booking.setStatus("PENDING");
        booking.setTotalCost(totalCost);
        booking = bookingRepository.save(booking);

        // 6. Call Payment Service
        try {
            Boolean paymentSuccess = webClientBuilder.build()
                    .post()
                    .uri("http://localhost:8085/payments")
                    .bodyValue(Map.of("bookingId", booking.getId(), "amount", totalCost))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            if (Boolean.TRUE.equals(paymentSuccess)) {
                // 7. Update Booking to CONFIRMED
                booking.setStatus("CONFIRMED");
                bookingRepository.save(booking);

                // 8. Update Inventory
                flightClient.bookFlight(flightId);
                hotelClient.bookHotel(hotelId);

                // 9. Call Notification Service
                webClientBuilder.build()
                        .post()
                        .uri("http://localhost:8084/notifications")
                        .bodyValue(Map.of("userId", userId, "message", "Booking Confirmed! ID: " + booking.getId()))
                        .retrieve()
                        .toBodilessEntity()
                        .subscribe(); // Async notification
            } else {
                booking.setStatus("PAYMENT_FAILED");
                bookingRepository.save(booking);
            }
        } catch (Exception e) {
            booking.setStatus("FAILED");
            bookingRepository.save(booking);
            throw new RuntimeException("Booking failed during payment processing: " + e.getMessage());
        }

        return booking;
    }

    public java.util.List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public java.util.Optional<Booking> getBooking(Long id) {
        return bookingRepository.findById(id);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
