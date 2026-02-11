package com.example.HealthCareManagement.exception;

public class ResourcesNotFoundException extends RuntimeException{
    public ResourcesNotFoundException(String message) {
        super(message);
    }
}
