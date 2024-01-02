package com.chadiai.userservice;

import com.chadiai.userservice.dto.AuthResponse;
import com.chadiai.userservice.dto.UserResponse;
import com.chadiai.userservice.model.User;
import com.chadiai.userservice.repository.UserRepository;
import com.chadiai.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository,passwordEncoder);
    }

    @Test
    public void testGetUserById() {
        // Arrange
        User user = User.builder().id(1).firstName("John").lastName("Doe").email("john.doe@example.com").build();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        UserResponse response = userService.getUserById(1);

        // Assert
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
    }

    @Test
    public void testGetAuthUserByEmail() {
        // Arrange
        User user = User.builder().id(1).email("admin@example.com").isAdmin(true).build();
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));

        // Act
        AuthResponse response = userService.getAuthUserByEmail("admin@example.com");

        // Assert
        assertEquals(1, response.getUserId());
        assertEquals(true, response.isAdmin());
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        User user = User.builder().id(1).firstName("John").lastName("Doe").email("john.doe@example.com").build();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // Act
        UserResponse response = userService.getAllUsers().get(0);

        // Assert
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        when(userRepository.existsById(1)).thenReturn(true);

        // Act
        userService.deleteUser(1);

        // Assert
        verify(userRepository, times(1)).deleteById(1);
    }
}
