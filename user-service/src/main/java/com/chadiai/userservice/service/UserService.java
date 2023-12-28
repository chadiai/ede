package com.chadiai.userservice.service;

import com.chadiai.userservice.dto.UserResponse;
import com.chadiai.userservice.model.User;
import com.chadiai.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public UserResponse getUserById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(this::mapToUserResponse).orElse(null);
    }
    public UserResponse getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
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


}
