package com.travel.notificationservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping
    public void sendNotification(@RequestBody NotificationRequest request) {
        log.info("Sending notification to user {}: {}", request.getUserId(), request.getMessage());
    }

    public static class NotificationRequest {
        private Long userId;
        private String message;

        public NotificationRequest() {
        }

        public NotificationRequest(Long userId, String message) {
            this.userId = userId;
            this.message = message;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
