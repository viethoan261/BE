package hoan.com.springboot.services.impl;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
@Component
public class SessionDestroyedEventListener implements ApplicationListener<HttpSessionDestroyedEvent> {

    private final SessionRegistry sessionRegistry;

    public SessionDestroyedEventListener(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void onApplicationEvent(HttpSessionDestroyedEvent event) {
        String sessionId = event.getSession().getId();

        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);

        if (sessionInformation != null) {
            sessionInformation.expireNow();
        }
    }
}

