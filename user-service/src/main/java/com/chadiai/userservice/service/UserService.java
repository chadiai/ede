package com.chadiai.userservice.service;

import com.chadiai.userservice.dto.AuthResponse;
import com.chadiai.userservice.dto.UserRequest;
import com.chadiai.userservice.dto.UserResponse;
import com.chadiai.userservice.model.User;
import com.chadiai.userservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void loadData() {
        if(userRepository.count() <= 0){
            User user = User.builder()
                    .isAdmin(true)
                    .email("admin")
                    .password(passwordEncoder.encode("admin"))
                    .firstName("admin")
                    .lastName("admin")
                    .build();
            userRepository.save(user);
        }
    }
    public UserResponse getUserById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(this::mapToUserResponse).orElse(null);
    }
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }


    public AuthResponse getAuthUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(this::mapToAuthResponse).orElse(null);
    }

    private AuthResponse mapToAuthResponse(User user) {
        return AuthResponse.builder()
                .userId(user.getId())
                .isAdmin(user.isAdmin())
                .build();
    }
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserResponse).toList();
    }

    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
        userRepository.deleteById(id);
    }

    public void editUser(int id, UserRequest editUserRequest) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Update user fields based on the editUserRequest
            if (editUserRequest.getFirstName() != null) {
                user.setFirstName(editUserRequest.getFirstName());
            }
            if (editUserRequest.getLastName() != null) {
                user.setLastName(editUserRequest.getLastName());
            }
            if (editUserRequest.getEmail() != null) {
                user.setEmail(editUserRequest.getEmail());
            }
            if (editUserRequest.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(editUserRequest.getPassword()));
            }
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
    }
}
