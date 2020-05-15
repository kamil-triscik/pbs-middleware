package com.pbs.middleware.server.features.template.listeners;

import com.pbs.middleware.server.common.domain.MiddlewarePublisher;
import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.features.template.events.TemplateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TemplatePublisher extends MiddlewarePublisher<TemplateEvent, String> {

    public TemplatePublisher(ApplicationEventPublisher applicationEventPublisher, EventRepository<TemplateEvent, String> repository) {
        super(applicationEventPublisher, repository);
    }

}
