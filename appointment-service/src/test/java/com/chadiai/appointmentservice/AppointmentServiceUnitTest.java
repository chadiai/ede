package com.chadiai.appointmentservice;

import com.chadiai.appointmentservice.dto.AppointmentRequest;
import com.chadiai.appointmentservice.dto.EditAppointmentRequest;
import com.chadiai.appointmentservice.model.Appointment;
import com.chadiai.appointmentservice.model.Status;
import com.chadiai.appointmentservice.repository.AppointmentRepository;
import com.chadiai.appointmentservice.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceUnitTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appointmentService = new AppointmentService(appointmentRepository);
    }

    @Test
    public void testCreateAppointment() {
        // Arrange
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setInitiatorId(1);
        appointmentRequest.setResponderId(2);
        appointmentRequest.setStatus(Status.SCHEDULED);
        appointmentRequest.setTitle("New Appointment");
        appointmentRequest.setDate(new Date());
        appointmentRequest.setLocation("C103");

        // Act
        appointmentService.createAppointment(appointmentRequest);

        // Assert
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    public void testDeleteAppointment() {
        // Arrange
        int appointmentId = 1;
        when(appointmentRepository.existsById(appointmentId)).thenReturn(true);

        // Act
        appointmentService.deleteAppointment(appointmentId);

        // Assert
        verify(appointmentRepository, times(1)).deleteById(appointmentId);
    }

    @Test
    public void testGetAllAppointmentsByUserId() {
        // Arrange
        int userId = 1;
        Appointment appointment = new Appointment();
        appointment.setInitiatorId(userId);
        appointment.setResponderId(2);
        appointment.setStatus(Status.SCHEDULED);
        appointment.setTitle("Test Appointment");
        appointment.setDate(new Date());
        appointment.setLocation("C102");

        when(appointmentRepository.findAll()).thenReturn(Collections.singletonList(appointment));

        // Act
        var appointments = appointmentService.getAllAppointmentsByUserId(userId);

        // Assert
        assertEquals(1, appointments.size());
        assertEquals("Test Appointment", appointments.get(0).getTitle());
    }

    @Test
    public void testEditAppointment() {
        // Arrange
        int appointmentId = 1;
        EditAppointmentRequest editRequest = new EditAppointmentRequest();
        editRequest.setStatus(Status.CANCELED);
        editRequest.setTitle("Edited Appointment");

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setStatus(Status.SCHEDULED);
        appointment.setTitle("Original Appointment");

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        // Act
        appointmentService.editAppointment(appointmentId, editRequest);

        // Assert
        assertEquals(Status.CANCELED, appointment.getStatus());
        assertEquals("Edited Appointment", appointment.getTitle());
    }
}
