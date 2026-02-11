package com.example.HealthCareManagement.controller;

import com.example.HealthCareManagement.entity.Availability;
import com.example.HealthCareManagement.entity.User;
import com.example.HealthCareManagement.service.DoctorSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class DoctorSearchController {

    private final DoctorSearchService searchService;

    public DoctorSearchController(DoctorSearchService searchService) {
        this.searchService = searchService;
    }

    // STEP 1: Find Doctors (We already have this logic)
    @GetMapping("/specialty")
    public ResponseEntity<List<User>> searchBySpecialty(@RequestParam String query) {
        return ResponseEntity.ok(searchService.searchBySpecialization(query));
    }

    // STEP 2: View Free Slots for the chosen Doctor
    // This is how the patient "knows" the slots!
    @GetMapping("/slots/{doctorId}")
    public ResponseEntity<List<Availability>> getAvailableSlots(@PathVariable Long doctorId) {
        return ResponseEntity.ok(searchService.getAvailableSlots(doctorId));
    }
}