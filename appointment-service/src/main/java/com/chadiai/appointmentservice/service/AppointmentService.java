package com.chadiai.appointmentservice.service;

import com.chadiai.appointmentservice.dto.AppointmentRequest;
import com.chadiai.appointmentservice.dto.AppointmentResponse;
import com.chadiai.appointmentservice.dto.EditAppointmentRequest;
import com.chadiai.appointmentservice.model.Appointment;
import com.chadiai.appointmentservice.model.Status;
import com.chadiai.appointmentservice.repository.AppointmentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    @PostConstruct
    public void loadData() {
        if(appointmentRepository.count() <= 0){
            Appointment appointment1 = Appointment.builder()
                    .initiatorId(1)
                    .responderId(2)
                    .status(Status.CANCELED)
                    .date(new Date())
                    .title("Appointment 1")
                    .location("C102")
                    .build();
            Appointment appointment2 = Appointment.builder()
                    .initiatorId(1)
                    .responderId(2)
                    .status(Status.SCHEDULED)
                    .date(new Date())
                    .title("Appointment 2")
                    .location("C102")
                    .build();

            appointmentRepository.save(appointment1);
            appointmentRepository.save(appointment2);
        }
    }

    public void createAppointment(AppointmentRequest appointmentRequest){
        Appointment appointment = Appointment.builder()
                .initiatorId(appointmentRequest.getInitiatorId())
                .responderId(appointmentRequest.getResponderId())
                .status(appointmentRequest.getStatus())
                .title(appointmentRequest.getTitle())
                .date(appointmentRequest.getDate())
                .location(appointmentRequest.getLocation())
                .build();

        appointmentRepository.save(appointment);
    }

    public void deleteAppointment(int id) {
        if (!appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Appointment with ID " + id + " not found.");
        }
        appointmentRepository.deleteById(id);
    }

    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .status(appointment.getStatus())
                .initiatorId(appointment.getInitiatorId())
                .responderId(appointment.getResponderId())
                .title(appointment.getTitle())
                .date(appointment.getDate())
                .location(appointment.getLocation())
                .build();
    }

    public List<AppointmentResponse> getAllAppointmentsByUserId(int id) {
        List<Appointment> allAppointments = appointmentRepository.findAll();

        return allAppointments.stream()
                .filter(appointment -> appointment.getInitiatorId() == id || appointment.getResponderId() == id)
                .map(this::mapToAppointmentResponse)
                .toList();
    }

    public List<AppointmentResponse> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream().map(this::mapToAppointmentResponse).toList();
    }

    public void editAppointment(int id, EditAppointmentRequest editRequest) {
        // Retrieve the appointment by its ID
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            if (editRequest.getStatus() != null) {
                appointment.setStatus(editRequest.getStatus());
            }
            if (editRequest.getTitle() != null) {
                appointment.setTitle(editRequest.getTitle());
            }
            if (editRequest.getDate() != null) {
                appointment.setDate(editRequest.getDate());
            }
            if (editRequest.getLocation() != null) {
                appointment.setLocation(editRequest.getLocation());
            }

            // Save the updated appointment back to the database
            appointmentRepository.save(appointment);
        } else {
            throw new IllegalArgumentException("Appointment with ID " + id + " not found.");
        }
    }
}
