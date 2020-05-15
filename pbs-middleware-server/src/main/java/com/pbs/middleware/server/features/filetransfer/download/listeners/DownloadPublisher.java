package com.pbs.middleware.server.features.filetransfer.download.listeners;

import com.pbs.middleware.server.common.domain.MiddlewarePublisher;
import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadEvent;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DownloadPublisher extends MiddlewarePublisher<DownloadEvent, UUID> {

    public DownloadPublisher(ApplicationEventPublisher applicationEventPublisher, EventRepository<DownloadEvent, UUID> repository) {
        super(applicationEventPublisher, repository);
    }

}
