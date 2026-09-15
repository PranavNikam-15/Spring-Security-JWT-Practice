package practice.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import practice.auth.dtos.AuthResponse;
import practice.auth.dtos.LoginRequest;
import practice.auth.dtos.RegisterRequest;
import practice.auth.dtos.UserResponse;
import practice.auth.entity.User;
import practice.auth.exceptions.ResourceNotFoundException;
import practice.auth.repository.UserRepository;
import practice.auth.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    // Register User
    public UserResponse registerUser(RegisterRequest request) {
        return userService.createUser(request);
    }

    // Login User
    public AuthResponse loginUser(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("User not found."));

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, "Login successful");
    }
}
