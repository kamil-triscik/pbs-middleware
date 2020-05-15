package com.pbs.middleware.server.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class UserLoginLogger {

    private static final Logger log = LoggerFactory.getLogger(UserLoginLogger.class);

    @EventListener
    public void logEvent(AuditApplicationEvent event) {
        AuditEvent auditEvent = event.getAuditEvent();

        String user = auditEvent.getPrincipal();
        String type = auditEvent.getType();

        WebAuthenticationDetails details = (WebAuthenticationDetails) auditEvent.getData().get("details");
        String remoteIp = "-";
        String sessionId = "-";
        if (details != null) {
            remoteIp = details.getRemoteAddress();
            sessionId = details.getSessionId();
        }

        log.info("User: {}, Type: {}, Remote IP: {}, Session ID: {}", user, type, remoteIp, sessionId);
    }
}
