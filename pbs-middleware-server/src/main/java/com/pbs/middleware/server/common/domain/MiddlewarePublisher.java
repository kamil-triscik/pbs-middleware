package com.pbs.middleware.server.common.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.server.common.storage.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
@RequiredArgsConstructor
public class MiddlewarePublisher<EVENT extends MiddlewareEvent<ID>, ID> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ApplicationEventPublisher applicationEventPublisher;

    private final EventRepository<EVENT, ID> repository;

    public void publish(final EVENT event) {
        publish(event, false);
    }

    public void publishAsync(final EVENT event) {
       publish(event, true);
    }

    private void publish(final EVENT event, boolean asynchronous) {
        try {
            log.trace("Saving event {}", event.getEventId());
            EVENT storedEvent = repository.save(event);

            beforePublish(storedEvent);

            log.debug(
                    "Publishing event: " + storedEvent.getClass().getSimpleName()
                            + "\n" + new JSONObject(objectMapper.writeValueAsString(event)).toString(3)
            );

            if (asynchronous) {
                new Thread(() -> {
                    try {
                        MDC.put("objectId", "upload-" + event.getDomainId().toString());
                        applicationEventPublisher.publishEvent(event);
                    } finally {
                        MDC.remove("objectId");
                    }
                }).start();
            } else {
                applicationEventPublisher.publishEvent(storedEvent);
            }
        } catch (Exception e) {
            log.error("Event " + event.getClass().getSimpleName() + " publishing failed", e);
        }
    }

    protected void beforePublish(final EVENT event) {

    }
}
