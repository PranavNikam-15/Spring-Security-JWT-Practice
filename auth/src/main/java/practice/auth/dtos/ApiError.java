package practice.auth.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
public class ApiError {
    private int status;
    private String error;
    private String message;
    private String path;
    private OffsetDateTime timestamp;
    private List<String> details;
}
