package com.example.HealthCareManagement.repository;

import com.example.HealthCareManagement.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    // This is the method the error is complaining about
    List<Availability> findByDoctorIdAndAvailableDate(Long doctorId, LocalDate availableDate);

    // You also need these for the other features we built
    List<Availability> findByDoctorId(Long doctorId);

    List<Availability> findByDoctorIdAndIsBookedFalse(Long doctorId);
}