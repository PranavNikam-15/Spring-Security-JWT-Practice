package practice.auth.dtos;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateUserRequest {

    @Size(min = 3, max = 15, message = "Name must be between 3 to 15 characters")
    private String name;

    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;
}