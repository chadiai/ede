package fact.it.notificationservice.controller;

import fact.it.notificationservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createProduct
            (@RequestBody fact.it.notificationservice.dto.AppointmentRequest appointmentRequest) {
        appointmentService.createAppointment(appointmentRequest);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<fact.it.notificationservice.dto.AppointmentResponse> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }
}

