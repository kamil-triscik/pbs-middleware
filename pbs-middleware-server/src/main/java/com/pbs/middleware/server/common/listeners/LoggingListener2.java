package com.pbs.middleware.server.common.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.server.common.domain.MiddlewareEvent;
import com.pbs.middleware.server.common.exception.MiddlewareException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingListener2 implements Ordered {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @EventListener
    public void onEvent(MiddlewareEvent<?> event2) throws MiddlewareException {
        try {
            log.debug("[" + event2.getDomainId() + "]Notified event: " + event2.getClass().getSimpleName() + "\n" + new JSONObject(objectMapper.writeValueAsString(event2)).toString(4));
        } catch (JsonProcessingException e) {
            log.error("Event logging error: " + e);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
