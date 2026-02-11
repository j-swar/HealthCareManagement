package com.example.HealthCareManagement.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "medical_histories")
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId; //

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient; //

    private String diagnosis; //
    private String treatment; //
    private LocalDate dateOfVisit; //

    public MedicalHistory() {}

    public Long getHistoryId() { return historyId; }
    public void setHistoryId(Long historyId) { this.historyId = historyId; }
    public User getPatient() { return patient; }
    public void setPatient(User patient) { this.patient = patient; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public LocalDate getDateOfVisit() { return dateOfVisit; }
    public void setDateOfVisit(LocalDate dateOfVisit) { this.dateOfVisit = dateOfVisit; }
}