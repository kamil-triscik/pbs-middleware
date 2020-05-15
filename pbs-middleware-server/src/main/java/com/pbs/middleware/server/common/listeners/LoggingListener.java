package com.pbs.middleware.server.common.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.server.common.domain.MiddlewareEvent;
import com.pbs.middleware.server.common.domain.MiddlewareEventListener;
import com.pbs.middleware.server.common.exception.MiddlewareException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
public abstract class LoggingListener<EVENT extends MiddlewareEvent> implements MiddlewareEventListener<EVENT> {

    private ObjectMapper objectMapper = new ObjectMapper();

    private final Object id;

    @Override
    public void onEvent(EVENT event) throws MiddlewareException {
        try {
            log.debug("[" + id + "]Notified event: " + event.getClass().getSimpleName() + "\n" + new JSONObject(objectMapper.writeValueAsString(event)).toString(4));
        } catch (JsonProcessingException e) {
            log.error("Event logging error: " + e);
        }
    }
}
