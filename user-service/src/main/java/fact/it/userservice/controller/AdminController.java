package fact.it.userservice.controller;

import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hi admin");
    }
    @GetMapping("/allusers")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }
}
