package practice.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(
            min = 3,
            max = 15,
            message = "Name must be between 3 and 15 characters"
    )
    private String name;


    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;


    @NotBlank(message = "Password is required")
    @Size(
            min = 8,
            message = "Password must contain at least 8 characters"
    )
    private String password;
}
