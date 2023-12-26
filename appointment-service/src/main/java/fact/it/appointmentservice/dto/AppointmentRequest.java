package fact.it.appointmentservice.dto;

import fact.it.appointmentservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {
    private String title;
    private Long consultantId;
    private Long clientId;
    private Date date;
    private Status status;
    private String location;
}