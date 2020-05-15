package com.pbs.middleware.server.features.job.listeners;

import com.pbs.middleware.server.common.domain.MiddlewarePublisher;
import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.features.job.events.JobEvent;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobPublisher extends MiddlewarePublisher<JobEvent, UUID> {

    public JobPublisher(ApplicationEventPublisher applicationEventPublisher, EventRepository<JobEvent, UUID> repository) {
        super(applicationEventPublisher, repository);
    }

}
