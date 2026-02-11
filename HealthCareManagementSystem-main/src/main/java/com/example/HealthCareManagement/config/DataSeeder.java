package com.example.HealthCareManagement.config;

import com.example.HealthCareManagement.entity.User;
import com.example.HealthCareManagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only seed if the database is empty to avoid duplicates [cite: 30]
        if (userRepository.count() == 0) {

            // 1. Seed a Doctor (Requirement 2.3) [cite: 13]
            User doctor = new User();
            doctor.setName("Dr. Strange");
            doctor.setEmail("strange@hospital.com");
            doctor.setPasswordHash("magic123");
            doctor.setRole("DOCTOR"); // Fixed the incompatible type here
            doctor.setSpecialization("Neurosurgeon");
            //another doctor
            User doctor1= new User();
            doctor1.setName("Swar Jadhav");
            doctor1.setEmail("swarjadhav325@gmail.com");
            doctor1.setPasswordHash("Swarom@1811");
            doctor1.setRole("DOCTOR");
            doctor1.setSpecialization("Cardiologist");
            // Requirement 14 [cite: 14]
            userRepository.save(doctor);
            userRepository.save(doctor1);

            // 2. Seed a Patient (Requirement 2.1) [cite: 7]
            User patient = new User();
            patient.setName("Peter Parker");
            patient.setEmail("peter@web.com");
            patient.setPasswordHash("spidey123");
            patient.setRole("PATIENT"); // Fixed the incompatible type here
            patient.setMedicalHistory("Allergic to spiders");
            patient.setBloodGroup("O+");// Requirement 8 [cite: 8]
            userRepository.save(patient);

            System.out.println("Hospital Database Seeded with Doctor and Patient!");
        }
    }
}