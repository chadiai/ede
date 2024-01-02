package com.chadiai.userservice.controller;

import com.chadiai.userservice.dto.AuthResponse;
import com.chadiai.userservice.dto.UserRequest;
import com.chadiai.userservice.dto.UserResponse;
import com.chadiai.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editUser(@PathVariable int id, @RequestBody UserRequest userRequest) {
        userService.editUser(id,userRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AuthResponse> getUserIdByEmail(@PathVariable String email) {
        AuthResponse user = userService.getAuthUserByEmail(email);
        return ResponseEntity.ok(user);
    }
}
