package com.example.HealthCareManagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultationId; // Matches LLD 4.3.3 [cite: 84]

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment; // Matches LLD 4.3.3 [cite: 49]

    @Column(columnDefinition = "TEXT")
    private String notes; // Matches LLD 4.3.3 [cite: 87]

    private String prescription; // Matches LLD 4.3.3 [cite: 16]

    private LocalDateTime dateOfVisit; // Matches LLD 4.4.2 [cite: 76]

    public Consultation() {}

    // --- GETTERS AND SETTERS ---

    public Long getConsultationId() { return consultationId; }
    public void setConsultationId(Long consultationId) { this.consultationId = consultationId; }

    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    // THIS WAS THE MISSING PIECE
    public LocalDateTime getDateOfVisit() { return dateOfVisit; }
    public void setDateOfVisit(LocalDateTime dateOfVisit) { this.dateOfVisit = dateOfVisit; }
}