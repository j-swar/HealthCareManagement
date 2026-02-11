package com.example.HealthCareManagement.controller;

import com.example.HealthCareManagement.entity.User;
import com.example.HealthCareManagement.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(authService.login(email, password));
    }

    @PutMapping("/{id}/update-profile")
    public ResponseEntity<User> updateDoctorProfile(@PathVariable Long id, @RequestBody User updatedInfo) {
        return ResponseEntity.ok(authService.updateDoctorProfile(id, updatedInfo));
    }

    @PutMapping("/{id}/update-patient")
    public ResponseEntity<User> updatePatientProfile(@PathVariable Long id, @RequestBody User updatedInfo) {
        return ResponseEntity.ok(authService.updatePatientProfile(id, updatedInfo));
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwordData) {

        authService.changePassword(id, passwordData.get("oldPassword"), passwordData.get("newPassword"));
        return ResponseEntity.ok("Password updated successfully.");
    }

    // 6. Logout
    // URL: POST http://localhost:8081/auth/logout/3
    @PostMapping("/logout/{id}")
    public ResponseEntity<String> logout(@PathVariable Long id) {
        authService.logout(id);
        return ResponseEntity.ok("User ID " + id + " logged out successfully. Session invalidated.");
    }
}