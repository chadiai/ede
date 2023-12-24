package fact.it.appointmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document(value = "appointment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Appointment {
    private Long id;
    private String title;
    private Date date;
    private String location;
}
