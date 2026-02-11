package com.example.HealthCareManagement.service;

import com.example.HealthCareManagement.entity.*;
import com.example.HealthCareManagement.repository.*;
import com.example.HealthCareManagement.exception.ResourcesNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultationService {

    // Logging framework setup for professional audit trailing
    private static final Logger logger = LoggerFactory.getLogger(ConsultationService.class);

    private final ConsultationRepository consultationRepo;
    private final AppointmentRepository appointmentRepo;
    private final NotificationRepository notificationRepo;

    // Constructor injection: Linking all required "Filing Cabinets"
    public ConsultationService(ConsultationRepository consultationRepo,
                               AppointmentRepository appointmentRepo,
                               NotificationRepository notificationRepo) {
        this.consultationRepo = consultationRepo;
        this.appointmentRepo = appointmentRepo;
        this.notificationRepo = notificationRepo;
    }

    /**
     * Requirement 4.3: Consultation Records Module [cite: 66]
     * This method saves medical records and triggers a patient notification.
     */
    @Transactional
    public Consultation addConsultation(Long appointmentId, String notes, String prescription) {
        logger.info("Attempting to record consultation for Appointment ID: {}", appointmentId);

        // 1. Validate Appointment Existence [cite: 46, 54]
        Appointment appt = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> {
                    logger.error("Fail: Appointment ID {} not found.", appointmentId);
                    return new ResourcesNotFoundException("Appointment not found");
                });

        // 2. Create the Consultation Entity [cite: 71, 72]
        Consultation consultation = new Consultation();
        consultation.setAppointment(appt);
        consultation.setNotes(notes);
        consultation.setPrescription(prescription);
        consultation.setDateOfVisit(LocalDateTime.now());

        // 3. Update Appointment Status to 'Completed'
        appt.setStatus("Completed");
        appointmentRepo.save(appt);

        // 4. Save the Medical Record [cite: 68]
        Consultation savedConsultation = consultationRepo.save(consultation);
        logger.info("Consultation saved. ID: {}", savedConsultation.getConsultationId());

        // 5. Trigger Notification (Requirement 4.5: Notification and Reminder) [cite: 18, 77, 79]
        Notification notification = new Notification();
        notification.setRecipient(appt.getPatient());
        notification.setMessage("New medical record added by Dr. " + appt.getDoctor().getName() + ". Please check your history.");
        notification.setTimestamp(LocalDateTime.now());
        notificationRepo.save(notification);

        logger.info("Automated reminder sent to Patient ID: {}", appt.getPatient().getId());

        return savedConsultation;
    }

    /**
     * Requirement 4.3.1: View medical history [cite: 67, 68]
     */
    public List<Consultation> getHistory(Long patientId) {
        logger.info("Fetching medical history for Patient ID: {}", patientId);
        return consultationRepo.findByAppointmentPatientId(patientId);
    }
}