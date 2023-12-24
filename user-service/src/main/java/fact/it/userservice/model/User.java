package fact.it.userservice.model;
import lombok.*;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(value = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
