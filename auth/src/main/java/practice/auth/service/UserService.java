package practice.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import practice.auth.dtos.RegisterRequest;
import practice.auth.dtos.UpdateUserRequest;
import practice.auth.dtos.UserResponse;
import practice.auth.entity.Role;
import practice.auth.entity.User;
import practice.auth.exceptions.ResourceNotFoundException;
import practice.auth.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // create User
    public UserResponse createUser(RegisterRequest request) {

        if(request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setUserRoles(Set.of(Role.USER));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    // Get all users
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // Get user by id
    public UserResponse getUserById(String id) {

        if(id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id is required");
        }

        UUID userId = parseUserId(id);

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found!"));

        return mapToResponse(user);
    }


    // Get user by email
    public UserResponse getUserByEmail(String email) {

        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found!"));

        return mapToResponse(user);
    }

    public UserResponse updateUser(String id, UpdateUserRequest request) {

        if(id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id is required");
        }

        UUID userId = parseUserId(id);

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found!"));

        if(request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if(request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    // Delete user
    public void deleteUser(String id) {
        if(id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id is required");
        }
        UUID userId = parseUserId(id);

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found!");
        }

        userRepository.deleteById(userId);
    }


    public UserResponse mapToResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setUserRoles(user.getUserRoles());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }


    private UUID parseUserId(String userId) {
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid user id format."
            );
        }
    }
}