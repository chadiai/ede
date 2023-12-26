package fact.it.appointmentservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Long consultantId;
    private Long clientId;
    private Date date;
    private Status status;
    private String location;
}
