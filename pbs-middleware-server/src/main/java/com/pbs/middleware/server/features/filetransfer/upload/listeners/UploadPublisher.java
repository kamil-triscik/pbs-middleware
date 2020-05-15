package com.pbs.middleware.server.features.filetransfer.upload.listeners;

import com.pbs.middleware.server.common.domain.MiddlewarePublisher;
import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadEvent;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UploadPublisher extends MiddlewarePublisher<UploadEvent, UUID> {

    public UploadPublisher(ApplicationEventPublisher applicationEventPublisher, EventRepository<UploadEvent, UUID> repository) {
        super(applicationEventPublisher, repository);
    }

}
