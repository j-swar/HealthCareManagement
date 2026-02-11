package com.example.HealthCareManagement.repository;

import com.example.HealthCareManagement.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    // This looks inside the Appointment, then inside the Patient to find their ID
    List<Consultation> findByAppointmentPatientId(Long patientId);
}