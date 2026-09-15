package practice.auth.dtos;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
public class AuthResponse {

    private final String token;
    private final String message;
    private final OffsetDateTime timestamp;


    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
        this.timestamp = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
