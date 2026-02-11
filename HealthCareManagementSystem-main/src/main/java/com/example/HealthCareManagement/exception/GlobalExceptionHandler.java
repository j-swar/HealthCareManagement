package com.example.HealthCareManagement.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppointmentException.class)
    public ResponseEntity<Object> handleAppointmentException(AppointmentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // FIXED: Now it shows the actual database error instead of just "Duplicate Email"
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDatabaseConstraints(DataIntegrityViolationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());

        // Logic: If the error contains 'EMAIL', it's a duplicate. Otherwise, it's a logic error (like missing Doctor).
        String errorMessage = "Database Error: A constraint was violated.";
        if (ex.getMostSpecificCause().getMessage().contains("EMAIL")) {
            errorMessage = "Database Error: This email is already registered in the system.";
        } else if (ex.getMostSpecificCause().getMessage().contains("FOREIGN KEY")) {
            errorMessage = "Database Error: The related User or Doctor ID does not exist.";
        }

        body.put("message", errorMessage);
        body.put("status", HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}