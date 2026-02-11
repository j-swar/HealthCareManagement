package com.example.HealthCareManagement.service;

import com.example.HealthCareManagement.entity.Availability;
import com.example.HealthCareManagement.entity.User;
import com.example.HealthCareManagement.repository.AvailabilityRepository;
import com.example.HealthCareManagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorSearchService {

    private final UserRepository userRepo;
    private final AvailabilityRepository availabilityRepo;

    public DoctorSearchService(UserRepository userRepo, AvailabilityRepository availabilityRepo) {
        this.userRepo = userRepo;
        this.availabilityRepo = availabilityRepo;
    }

    // List all doctors
    public List<User> getAllDoctors() {
        return userRepo.findByRole("DOCTOR");
    }

    // Filter doctors by specialization
    public List<User> searchBySpecialization(String specialization) {
        return userRepo.findByRoleAndSpecializationContainingIgnoreCase("DOCTOR", specialization);
    }

    // Get only free slots for a specific doctor
    public List<Availability> getAvailableSlots(Long doctorId) {
        return availabilityRepo.findByDoctorIdAndIsBookedFalse(doctorId);
    }
}