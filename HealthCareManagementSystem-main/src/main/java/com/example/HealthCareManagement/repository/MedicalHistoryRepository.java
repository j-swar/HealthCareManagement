package com.example.HealthCareManagement.repository;

import com.example.HealthCareManagement.entity.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    // Allows doctors to retrieve a specific patient's past visits [cite: 17, 68]
    List<MedicalHistory> findByPatientId(Long patientId);
}