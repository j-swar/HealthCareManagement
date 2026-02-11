package com.example.HealthCareManagement.controller;

import com.example.HealthCareManagement.entity.Consultation;
import com.example.HealthCareManagement.service.ConsultationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {
    private final ConsultationService service;

    public ConsultationController(ConsultationService s) { this.service = s; }

    @PostMapping("/add")
    public Consultation add(@RequestParam Long apptId, @RequestParam String notes, @RequestParam String rx) {
        return service.addConsultation(apptId, notes, rx);
    }

    @GetMapping("/history/{patientId}")
    public List<Consultation> history(@PathVariable Long patientId) {
        return service.getHistory(patientId);
    }
}