package com.example.HealthCareManagement.entity;
import com.example.HealthCareManagement.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Matches AppointmentID

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient; // Link to PatientID

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor; // Link to DoctorID

    @ManyToOne
    @JoinColumn(name = "availability_id")
    private Availability availability;

    private String status; // Scheduled, Cancelled, Completed
    private LocalDateTime createdAt;
    private String comments;

    public Appointment() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getPatient() { return patient; }
    public void setPatient(User patient) { this.patient = patient; }
    public User getDoctor() { return doctor; }
    public void setDoctor(User doctor) { this.doctor = doctor; }
    public Availability getAvailability() { return availability; }
    public void setAvailability(Availability availability) { this.availability = availability; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}