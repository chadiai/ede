package fact.it.userservice.controller;

import fact.it.userservice.dto.JwtAuthResponse;
import fact.it.userservice.dto.RefreshTokenRequest;
import fact.it.userservice.dto.SignUpRequest;
import fact.it.userservice.dto.SigninRequest;
import fact.it.userservice.model.User;
import fact.it.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authService.signup(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signin(@RequestBody SigninRequest signinRequest){
        return ResponseEntity.ok(authService.signin(signinRequest));
    }
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
}
