package com.travel.paymentservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @PostMapping
    public boolean processPayment(@RequestBody PaymentRequest request) {
        log.info("Processing payment for booking {}: {}", request.getBookingId(), request.getAmount());
        return true; // Simulate successful payment
    }

    public static class PaymentRequest {
        private Long bookingId;
        private Double amount;

        public PaymentRequest() {
        }

        public PaymentRequest(Long bookingId, Double amount) {
            this.bookingId = bookingId;
            this.amount = amount;
        }

        public Long getBookingId() {
            return bookingId;
        }

        public void setBookingId(Long bookingId) {
            this.bookingId = bookingId;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }
    }
}
