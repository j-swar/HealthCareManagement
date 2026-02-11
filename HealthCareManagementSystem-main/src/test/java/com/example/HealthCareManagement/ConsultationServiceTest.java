package com.example.HealthCareManagement;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.HealthCareManagement.entity.*;
import com.example.HealthCareManagement.exception.ResourcesNotFoundException;
import com.example.HealthCareManagement.repository.*;
import com.example.HealthCareManagement.exception.AppointmentException;
import com.example.HealthCareManagement.service.ConsultationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ConsultationServiceTest {

    @Mock
    private AppointmentRepository appointmentRepo;

    @Mock
    private ConsultationRepository consultationRepo;

    @Mock
    private NotificationRepository notificationRepo; // Requirement 4.5 [cite: 82]

    @InjectMocks
    private ConsultationService consultationService;

    @Test
    public void testAddConsultation_Success() {
        // 1. ARRANGE: Set up a complete scenario with Doctor and Patient [cite: 36, 61]
        User doctor = new User();
        doctor.setName("Dr. Strange");

        User patient = new User();
        patient.setId(2L);
        patient.setName("Peter Parker");

        Appointment mockAppt = new Appointment();
        mockAppt.setId(10L);
        mockAppt.setStatus("Scheduled"); // Initial Status [cite: 54]
        mockAppt.setDoctor(doctor);
        mockAppt.setPatient(patient);

        // Tell the mocks how to behave
        when(appointmentRepo.findById(10L)).thenReturn(Optional.of(mockAppt));
        when(consultationRepo.save(any(Consultation.class))).thenAnswer(i -> i.getArguments()[0]);
        // notificationRepo.save will return the notification sent to it
        when(notificationRepo.save(any(Notification.class))).thenAnswer(i -> i.getArguments()[0]);

        // 2. ACT: Record the consultation details [cite: 67]
        Consultation result = consultationService.addConsultation(10L, "Mild Flu", "Rest and Fluids");

        // 3. ASSERT: Verify all requirements are met
        assertNotNull(result, "The consultation should be saved successfully.");
        assertEquals("Mild Flu", result.getNotes(), "Notes should match the input.");

        // Check if Status updated to 'Completed' (Requirement 54) [cite: 54]
        assertEquals("Completed", mockAppt.getStatus(), "Appointment status must be updated to Completed.");

        // Verify that an automated notification was triggered (Requirement 4.5.1) [cite: 79]
        verify(notificationRepo, times(1)).save(any(Notification.class));

        // Verify repositories were called to save data [cite: 34, 100]
        verify(consultationRepo, times(1)).save(any(Consultation.class));
        verify(appointmentRepo, times(1)).save(mockAppt);
    }

    @Test
    public void testAddConsultation_AppointmentNotFound() {
        // 1. ARRANGE: Mock an empty response for a fake ID
        when(appointmentRepo.findById(99L)).thenReturn(Optional.empty());

        // 2. ACT & ASSERT: Change AppointmentException to ResourcesNotFoundException
        assertThrows(ResourcesNotFoundException.class, () -> {
            consultationService.addConsultation(99L, "Notes", "Rx");
        });
    }
}