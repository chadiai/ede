package fact.it.notificationservice.service;

import fact.it.notificationservice.model.Appointment;
import fact.it.notificationservice.repository.AppointmentRepository;
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
            Appointment appointment1 = new Appointment();
            appointment1.setTitle("Meeting 1");
            appointment1.setDate(new Date()); // Set the date appropriately
            appointment1.setLocation("Location 1");

            Appointment appointment2 = new Appointment();
            appointment2.setTitle("Meeting 2");
            appointment2.setDate(new Date()); // Set the date appropriately
            appointment2.setLocation("Location 2");

            appointmentRepository.save(appointment1);
            appointmentRepository.save(appointment2);
        }
    }

    public void createAppointment(fact.it.notificationservice.dto.AppointmentRequest appointmentRequest){
        Appointment appointment = Appointment.builder()
                .title(appointmentRequest.getTitle())
                .date(appointmentRequest.getDate())
                .location(appointmentRequest.getLocation())
                .build();

        appointmentRepository.save(appointment);
    }

    public List<fact.it.notificationservice.dto.AppointmentResponse> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.stream().map(this::mapToAppointmentResponse).toList();
    }


    private fact.it.notificationservice.dto.AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        return fact.it.notificationservice.dto.AppointmentResponse.builder()
                .id(appointment.getId())
                .title(appointment.getTitle())
                .date(appointment.getDate())
                .location(appointment.getLocation())
                .build();
    }

}
