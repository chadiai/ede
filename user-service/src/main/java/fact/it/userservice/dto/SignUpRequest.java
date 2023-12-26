package fact.it.userservice.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String firstname;
    private String lastName;
    private String email;
    private String password;
}
