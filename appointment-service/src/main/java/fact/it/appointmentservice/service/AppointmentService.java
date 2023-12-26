package fact.it.appointmentservice.service;

import fact.it.appointmentservice.dto.AppointmentRequest;
import fact.it.appointmentservice.dto.AppointmentResponse;
import fact.it.appointmentservice.model.Appointment;
import fact.it.appointmentservice.model.Status;
import fact.it.appointmentservice.repository.AppointmentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @PostConstruct
    public void loadData() {
        if(appointmentRepository.count() <= 0){
            Appointment appointment1 = Appointment.builder()
                    .consultantId(1L)
                    .clientId(2L)
                    .status(Status.CANCELED)
                    .date(new Date())
                    .title("Appointment 1")
                    .location("C102")
                    .build();
            Appointment appointment2 = Appointment.builder()
                    .consultantId(1L)
                    .clientId(2L)
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
                .consultantId(appointmentRequest.getConsultantId())
                .clientId(appointmentRequest.getClientId())
                .status(appointmentRequest.getStatus())
                .title(appointmentRequest.getTitle())
                .date(appointmentRequest.getDate())
                .location(appointmentRequest.getLocation())
                .build();

        appointmentRepository.save(appointment);
    }

    public List<AppointmentResponse> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.stream().map(this::mapToAppointmentResponse).toList();
    }


    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .status(appointment.getStatus())
                .clientId(appointment.getClientId())
                .consultantId(appointment.getConsultantId())
                .title(appointment.getTitle())
                .date(appointment.getDate())
                .location(appointment.getLocation())
                .build();
    }

}
