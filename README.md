# Smart Travel Booking Platform

Assignment 02 - Distributed Travel Booking Backend Platform

## Overview
This project implements a distributed travel booking system consisting of 6 microservices. It demonstrates inter-service communication using **WebClient** and **Feign Client** in Spring Boot 3+.

## Microservices

| Service | Port | Description | Communication |
|---------|------|-------------|---------------|
| **Booking Service** | 8080 | Main Orchestrator | Feign (Flight, Hotel), WebClient (User, Payment, Notification) |
| **User Service** | 8081 | Manages Users | - |
| **Flight Service** | 8082 | Manages Flights | - |
| **Hotel Service** | 8083 | Manages Hotels | - |
| **Notification Service** | 8084 | Sends Notifications | - |
| **Payment Service** | 8085 | Processes Payments | - |

## Architecture Diagram


## Prerequisites
- Java 17+
- Maven

## How to Run
1. Navigate to each service directory and run:
   ```bash
   mvn spring-boot:run
   ```
   Start them in this order:
   1. User Service
   2. Flight Service
   3. Hotel Service
   4. Notification Service
   5. Payment Service
   6. Booking Service

## Testing
Use the provided `postman_collection.json` to test the endpoints.

### Booking Flow
1. **Create Booking**:
   `POST http://localhost:8080/bookings`
   ```json
   {
       "userId": 1,
       "flightId": 1,
       "hotelId": 1,
       "travelDate": "2025-01-10"
   }
   ```
2. **Response**:
   ```json
   {
       "id": 1,
       "userId": 1,
       "flightId": 1,
       "hotelId": 1,
       "travelDate": "2025-01-10",
       "status": "CONFIRMED",
       "totalCost": 700.0
   }
   ```
