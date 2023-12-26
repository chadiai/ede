package fact.it.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
