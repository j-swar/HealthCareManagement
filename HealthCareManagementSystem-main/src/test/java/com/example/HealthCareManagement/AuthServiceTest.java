package com.example.HealthCareManagement;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.HealthCareManagement.entity.User;
import com.example.HealthCareManagement.repository.UserRepository;
import com.example.HealthCareManagement.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository; // The "Stunt Double" for the database

    @InjectMocks
    private AuthService authService; // The real "Brain" we are testing

    @Test
    public void testRegisterPatient_Success() {
        // 1. ARRANGE: Create a fake patient based on Module 4.1.3 [cite: 35, 36]
        User patient = new User();
        patient.setName("Peter Parker");
        patient.setEmail("peter@web.com");

        // Tell the mock to return the patient when save() is called [cite: 34]
        when(userRepository.save(any(User.class))).thenReturn(patient);

        // 2. ACT: Call the service method
        User savedUser = authService.register(patient);

        // 3. ASSERT: Check if the name matches [cite: 40]
        assertNotNull(savedUser);
        assertEquals("Peter Parker", savedUser.getName());
        verify(userRepository, times(1)).save(patient); // Proves it reached the database layer [cite: 27]
    }
}