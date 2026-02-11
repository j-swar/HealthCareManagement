package com.example.HealthCareManagement.service;

import com.example.HealthCareManagement.entity.*;
import com.example.HealthCareManagement.exception.AppointmentException;
import com.example.HealthCareManagement.entity.Notification;
import com.example.HealthCareManagement.repository.*;
import com.example.HealthCareManagement.utility.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private final AppointmentRepository appointmentRepo;
    @Autowired
    private final AvailabilityRepository availabilityRepo;
    @Autowired
    private final UserRepository userRepo;
    @Autowired
    private final MedicalHistoryRepository medicalHistoryRepo;
    @Autowired// ADDED THIS
    private final SessionManager sessionManager;

    public AppointmentService(AppointmentRepository appointmentRepo,
                              AvailabilityRepository availabilityRepo,
                              UserRepository userRepo,
                              MedicalHistoryRepository medicalHistoryRepo, // ADDED THIS
                              SessionManager sessionManager) {
        this.appointmentRepo = appointmentRepo;
        this.availabilityRepo = availabilityRepo;
        this.userRepo = userRepo;
        this.medicalHistoryRepo = medicalHistoryRepo; // ADDED THIS
        this.sessionManager = sessionManager;
    }

    @Transactional
    public Appointment bookAppointment(Long patientId, Long slotId, String initialComment) {
        if (!sessionManager.isSessionActive(patientId)) {
            throw new AppointmentException("Access Denied: Please login first.");
        }

        User patient = userRepo.findById(patientId)
                .orElseThrow(() -> new AppointmentException("Patient not found."));

        Availability slot = availabilityRepo.findById(slotId)
                .orElseThrow(() -> new AppointmentException("Slot not found."));

        if (slot.isBooked()) {
            throw new AppointmentException("This slot is already booked.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(slot.getDoctor());
        appointment.setAvailability(slot);
        appointment.setStatus("Scheduled"); // Matches LLD Status
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setComments("Booking Note: " + (initialComment != null ? initialComment : "N/A"));

        slot.setBooked(true);
        availabilityRepo.save(slot);
        return appointmentRepo.save(appointment);
    }

    @Transactional
    public void cancelAppointment(Long userId, Long appointmentId, String reason) {
        if (!sessionManager.isSessionActive(userId)) {
            throw new AppointmentException("Login required.");
        }

        Appointment appt = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new AppointmentException("Appointment not found."));

        if (!appt.getPatient().getId().equals(userId) && !appt.getDoctor().getId().equals(userId)) {
            throw new AppointmentException("Unauthorized.");
        }

        Availability slot = appt.getAvailability();
        slot.setBooked(false);
        availabilityRepo.save(slot);

        appt.setStatus("Cancelled"); // Matches LLD Status
        appt.setComments("Cancelled. Reason: " + reason);
        appointmentRepo.save(appt);
    }

    @Transactional
    public Appointment reschedule(Long userId, Long appointmentId, Long newSlotId, String reason) {
        if (!sessionManager.isSessionActive(userId)) {
            throw new AppointmentException("Login required.");
        }

        Appointment appt = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new AppointmentException("Appointment not found."));

        Availability newSlot = availabilityRepo.findById(newSlotId)
                .orElseThrow(() -> new AppointmentException("New slot not found."));

        if (newSlot.isBooked()) throw new AppointmentException("New slot taken.");

        appt.getAvailability().setBooked(false);
        availabilityRepo.save(appt.getAvailability());

        newSlot.setBooked(true);
        availabilityRepo.save(newSlot);

        appt.setAvailability(newSlot);
        appt.setComments("Rescheduled. Reason: " + reason);
        return appointmentRepo.save(appt);
    }

    // --- NEW: MEDICAL HISTORY & COMPLETION MODULE [cite: 15-16, 54, 66-76] ---

    @Transactional
    public MedicalHistory completeAppointment(Long doctorId, Long appointmentId, String diagnosis, String treatment) {
        if (!sessionManager.isSessionActive(doctorId)) {
            throw new AppointmentException("Doctor must be logged in.");
        }

        Appointment appt = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new AppointmentException("Appointment not found."));

        if (!appt.getDoctor().getId().equals(doctorId)) {
            throw new AppointmentException("Unauthorized: You are not the assigned doctor.");
        }

        // 1. Update Appointment Status to COMPLETED
        appt.setStatus("Completed");
        appointmentRepo.save(appt);

        // 2. Create and Store Medical History Record [cite: 68, 71]
        MedicalHistory history = new MedicalHistory();
        history.setPatient(appt.getPatient());
        history.setDiagnosis(diagnosis); // [cite: 74]
        history.setTreatment(treatment); // [cite: 75]
        history.setDateOfVisit(LocalDate.now()); // [cite: 76]

        return medicalHistoryRepo.save(history);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepo.findByPatientId(patientId);
    }

    public List<Appointment> getDoctorAppointments(Long doctorId) {
        return appointmentRepo.findByDoctorId(doctorId);
    }

    // New retrieval method for Medical History records [cite: 68]
    public List<MedicalHistory> getPatientHistory(Long patientId) {
        return medicalHistoryRepo.findByPatientId(patientId);
    }
}