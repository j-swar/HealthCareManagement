package com.example.HealthCareManagement.controller;

import com.example.HealthCareManagement.entity.Appointment;
import com.example.HealthCareManagement.entity.MedicalHistory;
import com.example.HealthCareManagement.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/book/{patientId}/{slotId}")
    public ResponseEntity<Appointment> book(
            @PathVariable Long patientId,
            @PathVariable Long slotId,
            @RequestParam(required = false) String comment) {
        return ResponseEntity.ok(appointmentService.bookAppointment(patientId, slotId, comment));
    }

    @PutMapping("/cancel/{userId}/{appointmentId}")
    public ResponseEntity<String> cancel(
            @PathVariable Long userId,
            @PathVariable Long appointmentId,
            @RequestParam String reason) {
        appointmentService.cancelAppointment(userId, appointmentId, reason);
        return ResponseEntity.ok("Cancelled successfully.");
    }

    @PutMapping("/reschedule/{userId}/{appointmentId}/{newSlotId}")
    public ResponseEntity<Appointment> reschedule(
            @PathVariable Long userId,
            @PathVariable Long appointmentId,
            @PathVariable Long newSlotId,
            @RequestParam String reason) {
        return ResponseEntity.ok(appointmentService.reschedule(userId, appointmentId, newSlotId, reason));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getPatientAppts(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getDoctorAppts(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(doctorId));
    }

    // --- NEW METHODS ADDED BELOW TO MATCH LLD REQUIREMENTS ---

    // 1. Module 2.4: Complete Appointment & Record History [cite: 16, 54, 68]
    @PutMapping("/complete/{doctorId}/{appointmentId}")
    public ResponseEntity<MedicalHistory> complete(
            @PathVariable Long doctorId,
            @PathVariable Long appointmentId,
            @RequestParam String diagnosis,
            @RequestParam String treatment) {
        return ResponseEntity.ok(appointmentService.completeAppointment(doctorId, appointmentId, diagnosis, treatment));
    }

    // 2. Module 4.4: View Patient Medical History [cite: 17, 68]
    @GetMapping("/history/{patientId}")
    public ResponseEntity<List<MedicalHistory>> getHistory(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getPatientHistory(patientId));
    }
}