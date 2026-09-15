package practice.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import practice.auth.dtos.UserResponse;
import practice.auth.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Public
    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint";
    }

    // USER
    @GetMapping("/user")
    public String userEndpoint(Authentication authentication) {
        return "Hello USER : " + authentication.getName();
    }

    // ADMIN
    @GetMapping("/admin")
    public String adminEndpoint(Authentication authentication) {
        return "Hello ADMIN : " + authentication.getName();
    }


    @GetMapping("/user/me")
    public ResponseEntity<UserResponse> currentUser(Authentication authentication) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getUserByEmail(authentication.getName())
        );
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getAllUsers()
        );
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getUserById(id)
        );
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}