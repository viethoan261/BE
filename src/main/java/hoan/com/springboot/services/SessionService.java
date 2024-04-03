package hoan.com.springboot.services;

import hoan.com.springboot.payload.response.UserActivityInfo;
import org.springframework.security.core.session.SessionInformation;

import java.util.List;

public interface SessionService {
    List<UserActivityInfo> getAllSessions();
}
