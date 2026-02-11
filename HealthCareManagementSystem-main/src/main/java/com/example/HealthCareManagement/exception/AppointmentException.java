package com.example.HealthCareManagement.exception;

// We use RuntimeException so we don't have to clutter our code with 'throws'
public class AppointmentException extends RuntimeException {
    public AppointmentException(String message) {
        super(message);
    }
}