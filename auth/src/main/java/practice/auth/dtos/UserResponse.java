package practice.auth.dtos;

import lombok.Getter;
import lombok.Setter;
import practice.auth.entity.Role;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private Set<Role> userRoles;

    private Instant createdAt;
    private Instant updatedAt;
}