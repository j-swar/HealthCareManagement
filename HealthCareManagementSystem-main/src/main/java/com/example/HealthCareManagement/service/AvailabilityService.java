package com.example.HealthCareManagement.service;

import com.example.HealthCareManagement.dto.AvailabilityDTO;
import com.example.HealthCareManagement.entity.Availability;
import com.example.HealthCareManagement.entity.User;
import com.example.HealthCareManagement.exception.AppointmentException;
import com.example.HealthCareManagement.repository.AvailabilityRepository;
import com.example.HealthCareManagement.repository.UserRepository;
import com.example.HealthCareManagement.utility.SessionManager;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepo;
    private final UserRepository userRepo;
    private final SessionManager sessionManager;

    public AvailabilityService(AvailabilityRepository availabilityRepo, UserRepository userRepo, SessionManager sessionManager) {
        this.availabilityRepo = availabilityRepo;
        this.userRepo = userRepo;
        this.sessionManager = sessionManager;
    }

    public List<Availability> addBulkAvailability(Long doctorId, List<AvailabilityDTO> dtos) {
        if (!sessionManager.isSessionActive(doctorId)) {
            throw new AppointmentException("Access Denied: Please login first.");
        }

        User doctor = userRepo.findById(doctorId)
                .orElseThrow(() -> new AppointmentException("Doctor not found."));

        List<Availability> savedSlots = new ArrayList<>();

        for (AvailabilityDTO dto : dtos) {
            // 1. Check for Overlaps in MySQL for that specific date
            List<Availability> existingSlots = availabilityRepo.findByDoctorIdAndAvailableDate(doctorId, dto.getAvailableDate());

            for (Availability existing : existingSlots) {
                boolean overlaps = dto.getStartTime().isBefore(existing.getEndTime()) &&
                        existing.getStartTime().isBefore(dto.getEndTime());

                if (overlaps) {
                    throw new AppointmentException("Overlap detected for date " + dto.getAvailableDate() +
                            ". Slot " + dto.getStartTime() + "-" + dto.getEndTime() + " conflicts with an existing slot.");
                }
            }

            // 2. Map and Save
            Availability availability = new Availability();
            availability.setDoctor(doctor);
            availability.setAvailableDate(dto.getAvailableDate());
            availability.setStartTime(dto.getStartTime());
            availability.setEndTime(dto.getEndTime());
            availability.setBooked(false);
            savedSlots.add(availabilityRepo.save(availability));
        }

        return savedSlots;
    }

    public List<Availability> getMySlots(Long doctorId) {
        return availabilityRepo.findByDoctorId(doctorId);
    }
}