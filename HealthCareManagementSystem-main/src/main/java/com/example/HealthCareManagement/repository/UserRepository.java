package com.example.HealthCareManagement.repository;

import com.example.HealthCareManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // This helps us find someone by their email when they try to login [cite: 91]
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
    List<User> findByRoleAndSpecializationContainingIgnoreCase(String role, String specialization);
}

