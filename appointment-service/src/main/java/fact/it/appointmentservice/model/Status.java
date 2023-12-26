package fact.it.appointmentservice.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("scheduled")
    SCHEDULED,
    @JsonProperty("completed")
    COMPLETED,
    @JsonProperty("canceled")
    CANCELED,
}
