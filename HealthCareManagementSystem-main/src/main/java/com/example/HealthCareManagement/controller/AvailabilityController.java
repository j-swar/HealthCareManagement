package com.example.HealthCareManagement.controller;

import com.example.HealthCareManagement.dto.AvailabilityDTO;
import com.example.HealthCareManagement.entity.Availability;
import com.example.HealthCareManagement.service.AvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // POST: http://localhost:8081/api/availability/bulk-add/2
    @PostMapping("/bulk-add/{doctorId}")
    public ResponseEntity<List<Availability>> addBulkSlots(
            @PathVariable Long doctorId,
            @RequestBody List<AvailabilityDTO> dtos) {
        return ResponseEntity.ok(availabilityService.addBulkAvailability(doctorId, dtos));
    }

    @GetMapping("/my-slots/{doctorId}")
    public ResponseEntity<List<Availability>> getMySlots(@PathVariable Long doctorId) {
        return ResponseEntity.ok(availabilityService.getMySlots(doctorId));
    }
}