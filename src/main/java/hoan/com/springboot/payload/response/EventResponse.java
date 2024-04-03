package hoan.com.springboot.payload.response;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class EventResponse {
    private String fullName;

    private LocalDate dayOfBirth;

    private String avatarFileId;

    private LocalDate joinDate;
}
