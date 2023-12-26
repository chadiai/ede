package fact.it.userservice.service;

import fact.it.userservice.dto.JwtAuthResponse;
import fact.it.userservice.dto.RefreshTokenRequest;
import fact.it.userservice.dto.SignUpRequest;
import fact.it.userservice.dto.SigninRequest;
import fact.it.userservice.model.Role;
import fact.it.userservice.model.User;
import fact.it.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    public User signup(SignUpRequest signUpRequest){
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstname());
        user.setLastName(signUpRequest.getLastName());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        return userRepository.save(user);
    }
    public JwtAuthResponse signin(SigninRequest signinRequest){
        authManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),
                signinRequest.getPassword()));

        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();

        jwtAuthResponse.setToken(jwt);
        jwtAuthResponse.setRefreshToken(refreshToken);
        return jwtAuthResponse;
    }

    public JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            String jwt = jwtService.generateToken(user);
            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();

            jwtAuthResponse.setToken(jwt);
            jwtAuthResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthResponse;
        }
        return null;
    }
}
