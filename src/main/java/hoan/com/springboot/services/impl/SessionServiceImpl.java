package hoan.com.springboot.services.impl;

import hoan.com.springboot.payload.response.UserActivityInfo;
import hoan.com.springboot.services.SessionService;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {
    private final SessionRegistry sessionRegistry;

    public SessionServiceImpl(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public List<UserActivityInfo> getAllSessions() {
        List<UserActivityInfo> res = sessionRegistry.getAllPrincipals().stream()
                .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
                .map(principal -> {
                    UserDetails userDetails = (UserDetails) principal;

                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    SessionInformation sessionInformation = sessions.get(sessions.size() - 1);
                    String username = userDetails.getUsername();
//                    Date lastActivityTime = sessionInformation.getLastRequest();

                    Date lastActivityTime = sessionInformation.getLastRequest();

                    // Tạo một đối tượng Calendar
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(lastActivityTime);

                    // Tăng thời gian lên 7 giờ
                    calendar.add(Calendar.HOUR_OF_DAY, 7);

                    // Lấy thời gian sau khi tăng
                    Date modifiedActivityTime = calendar.getTime();

                    return new UserActivityInfo(username, modifiedActivityTime);
                })
                .collect(Collectors.toList());
        return res;
    }
}
