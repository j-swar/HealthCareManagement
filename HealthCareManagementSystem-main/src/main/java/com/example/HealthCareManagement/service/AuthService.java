package com.example.HealthCareManagement.service;

import com.example.HealthCareManagement.entity.User;
import com.example.HealthCareManagement.exception.AppointmentException;
import com.example.HealthCareManagement.repository.UserRepository;
import com.example.HealthCareManagement.utility.SessionManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final SessionManager sessionManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepo, SessionManager sessionManager, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.sessionManager = sessionManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Internal Guard: Prevents registration or login if a session is already active.
     */
    private void ensureNoActiveSession() {
        if (sessionManager.hasAnyActiveSession()) {
            throw new AppointmentException("An active session already exists. Please logout before trying to register or login again.");
        }
    }

    /**
     * REGISTER: Hashes password and saves user.
     * Guarded by ensureNoActiveSession.
     */
    public User register(User user) {
        ensureNoActiveSession();

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new AppointmentException("Email cannot be empty.");
        }

        String cleanEmail = user.getEmail().trim().toLowerCase();
        if (userRepo.findByEmail(cleanEmail).isPresent()) {
            throw new AppointmentException("User with this email already exists.");
        }

        // Fixed length check (Requires @JsonProperty in User entity)
        if (user.getPasswordHash() == null || user.getPasswordHash().trim().length() < 6) {
            throw new AppointmentException("Password must be at least 6 characters long.");
        }

        user.setEmail(cleanEmail);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash().trim()));

        return userRepo.save(user);
    }

    /**
     * LOGIN: Starts a session.
     * Guarded by ensureNoActiveSession.
     */
    public User login(String email, String password) {
        ensureNoActiveSession();

        if (email == null || password == null) {
            throw new AppointmentException("Email and password are required.");
        }

        User user = userRepo.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new AppointmentException("Invalid credentials."));

        if (!passwordEncoder.matches(password.trim(), user.getPasswordHash())) {
            throw new AppointmentException("Invalid credentials.");
        }

        sessionManager.startSession(user.getId());
        return user;
    }

    /**
     * LOGOUT: Ends a session.
     */
    public void logout(Long userId) {
        sessionManager.endSession(userId);
    }

    /**
     * CHANGE PASSWORD: Requires active session and old password verification.
     */
    public void changePassword(Long userId, String oldPass, String newPass) {
        if (!sessionManager.isSessionActive(userId)) {
            throw new AppointmentException("Access Denied: Please login first.");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppointmentException("User not found."));

        if (!passwordEncoder.matches(oldPass.trim(), user.getPasswordHash())) {
            throw new AppointmentException("Current password provided is incorrect.");
        }

        if (newPass == null || newPass.trim().length() < 6) {
            throw new AppointmentException("New password must be at least 6 characters long.");
        }

        user.setPasswordHash(passwordEncoder.encode(newPass.trim()));
        userRepo.save(user);
    }

    /**
     * UPDATE DOCTOR PROFILE: Handles specialization and experience.
     */
    public User updateDoctorProfile(Long userId, User updatedInfo) {
        if (!sessionManager.isSessionActive(userId)) {
            throw new AppointmentException("Access Denied: Please login first.");
        }

        User doctor = userRepo.findById(userId)
                .orElseThrow(() -> new AppointmentException("Doctor profile not found."));

        if (!"DOCTOR".equalsIgnoreCase(doctor.getRole())) {
            throw new AppointmentException("Unauthorized: Only Doctors can update this profile.");
        }

        if (updatedInfo.getSpecialization() != null) doctor.setSpecialization(updatedInfo.getSpecialization());
        if (updatedInfo.getExperienceYears() != null) doctor.setExperienceYears(updatedInfo.getExperienceYears());
        if (updatedInfo.getBio() != null) doctor.setBio(updatedInfo.getBio());
        if (updatedInfo.getName() != null) doctor.setName(updatedInfo.getName());
        if (updatedInfo.getPhone() != null) doctor.setPhone(updatedInfo.getPhone());

        return userRepo.save(doctor);
    }

    /**
     * UPDATE PATIENT PROFILE: Handles medical history and blood group.
     */
    public User updatePatientProfile(Long userId, User updatedInfo) {
        if (!sessionManager.isSessionActive(userId)) {
            throw new AppointmentException("Access Denied: Please login first.");
        }

        User patient = userRepo.findById(userId)
                .orElseThrow(() -> new AppointmentException("Patient profile not found."));

        if (!"PATIENT".equalsIgnoreCase(patient.getRole())) {
            throw new AppointmentException("Unauthorized: Only Patients can update this profile.");
        }

        if (updatedInfo.getMedicalHistory() != null) patient.setMedicalHistory(updatedInfo.getMedicalHistory());
        if (updatedInfo.getBloodGroup() != null) patient.setBloodGroup(updatedInfo.getBloodGroup());
        if (updatedInfo.getGender() != null) patient.setGender(updatedInfo.getGender());
        if (updatedInfo.getDateOfBirth() != null) patient.setDateOfBirth(updatedInfo.getDateOfBirth());
        if (updatedInfo.getName() != null) patient.setName(updatedInfo.getName());

        return userRepo.save(patient);
    }
}