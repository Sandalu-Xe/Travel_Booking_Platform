# Smart Travel Booking Platform

Assignment 02 - Distributed Travel Booking Backend Platform

## Overview
This project implements a distributed travel booking system consisting of 6 microservices. It demonstrates inter-service communication using **WebClient** and **Feign Client** in Spring Boot 3+.

## Microservices

| Service | Port | Description | Communication |
|---------|------|-------------|---------------|
| **Booking Service** | 8080 | **Orchestrator**. Manages the entire booking flow, coordinating user, flight, hotel, payment, and notification services. | Feign (Flight, Hotel), WebClient (User, Payment, Notification) |
| **User Service** | 8081 | Manages User profiles and data. | - |
| **Flight Service** | 8082 | Manages Flight inventory and availability. | - |
| **Hotel Service** | 8083 | Manages Hotel inventory and availability. | - |
| **Notification Service** | 8084 | handles sending notifications. **Currently logs messages to the console/file.** | - |
| **Payment Service** | 8085 | Simulates payment processing. | - |

## Architecture Diagram

```mermaid
graph TD
    User[Client] -->|POST /bookings| BookingService
    BookingService -->|GET /users/{id}| UserService
    BookingService -->|GET /flights/{id}| FlightService
    BookingService -->|GET /hotels/{id}| HotelService
    BookingService -->|POST /payments| PaymentService
    BookingService -->|POST /notifications| NotificationService
    BookingService -->|PUT /flights/{id}/book| FlightService
    BookingService -->|PUT /hotels/{id}/book| HotelService
```

## Prerequisites
- Java 17+
- Maven

## How to Run
1. Navigate to the `smart-travel-platform` directory.
2. Run each service in a separate terminal. You can use the following command in each service directory:
   ```bash
   mvn spring-boot:run
   ```
   **Recommended Startup Order:**
   1. `user-service` (Port 8081)
   2. `flight-service` (Port 8082)
   3. `hotel-service` (Port 8083)
   4. `notification-service` (Port 8084)
   5. `payment-service` (Port 8085)
   6. `booking-service` (Port 8080)

## API Documentation & Testing
A `postman_collection.json` is included in the project root for easy testing. Import it into Postman to try the requests.

### 1. Booking Flow (Primary Use Case)
The `Booking Service` orchestrates the flow. When you create a booking:
1. Validates User, Flight, and Hotel.
2. Checks availability.
3. Process Payment.
4. **On Success**: Confirms booking, updates inventory, and **sends a notification**.

**Endpoint:** `POST http://localhost:8080/bookings`
```json
{
    "userId": 1,
    "flightId": 1,
    "hotelId": 1,
    "travelDate": "2025-01-10"
}
```

### 2. Notification Service
Notifications are sent automatically after a successful booking. You can also trigger them manually.

**Automatic:** Check the console logs of the `notification-service` after a booking.
**Manual Trigger:**
**Endpoint:** `POST http://localhost:8084/notifications`
```json
{
    "userId": 1,
    "message": "This is a manual notification test."
}
```

### 3. Other Key Endpoints

#### User Service
- **Create User**: `POST http://localhost:8081/users`
- **Get User**: `GET http://localhost:8081/users/{id}`

#### Flight Service
- **Create Flight**: `POST http://localhost:8082/flights`
- **Get Flight**: `GET http://localhost:8082/flights/{id}`

#### Hotel Service
- **Create Hotel**: `POST http://localhost:8083/hotels`
- **Get Hotel**: `GET http://localhost:8083/hotels/{id}`
