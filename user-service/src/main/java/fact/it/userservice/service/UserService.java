package fact.it.userservice.service;

import fact.it.userservice.dto.*;
import fact.it.userservice.model.Role;
import fact.it.userservice.model.User;
import fact.it.userservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @PostConstruct
    public void loadData() {
        if(userRepository.count() <= 0){
            User user = User.builder()
                    .active(true)
                    .firstName("admin")
                    .lastName("admin")
                    .password(new BCryptPasswordEncoder().encode("admin"))
                    .email("admin@example.com")
                    .role(Role.ADMIN)
                    .build();
            User user1 = User.builder()
                    .active(true)
                    .firstName("Bob")
                    .lastName("Rice")
                    .password(new BCryptPasswordEncoder().encode("bob123"))
                    .email("bob@example.com")
                    .role(Role.USER)
                    .build();
            User user2 = User.builder()
                    .active(true)
                    .firstName("Nick")
                    .lastName("Smith")
                    .password(new BCryptPasswordEncoder().encode("nick123"))
                    .role(Role.USER)
                    .email("nick@example.com")
                    .build();
            userRepository.save(user);
            userRepository.save(user1);
            userRepository.save(user2);
        }
    }

    public User createUser(UserRequest userRequest) {
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .build();
        return userRepository.save(newUser);
    }

    public List<fact.it.userservice.dto.UserResponse> getAllUsers() {
        List<User> appointments = userRepository.findAll();

        return appointments.stream().map(this::mapToMessageResponse).toList();
    }


    private fact.it.userservice.dto.UserResponse mapToMessageResponse(User user) {
        return fact.it.userservice.dto.UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }


    public UserDetailsService useDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
}
