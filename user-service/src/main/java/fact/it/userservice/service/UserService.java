package fact.it.userservice.service;

import fact.it.userservice.dto.*;
import fact.it.userservice.model.User;
import fact.it.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(UserRequest userRequest) {
        try {
            User newUser = new User();
            newUser.setUsername(userRequest.getUsername());
            newUser.setFirstName(userRequest.getFirstName());
            newUser.setLastName(userRequest.getLastName());
            newUser.setPassword(userRequest.getPassword());
            newUser.setEmail(userRequest.getEmail());

            return userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            // Handle database integrity violations (e.g., duplicate username or email)
            // You can log the error, provide a user-friendly message, or take other actions.
            return null; // Indicate that user creation failed
        }
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserResponse(
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }


}
