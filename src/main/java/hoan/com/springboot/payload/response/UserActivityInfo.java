package hoan.com.springboot.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class UserActivityInfo {
    private String username;
    private Date lastActivityTime;
}
