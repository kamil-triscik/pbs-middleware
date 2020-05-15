package com.pbs.middleware.server.features.ownership.listeners;

import com.pbs.middleware.server.features.filetransfer.download.events.DownloadInitialized;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadInitialized;
import com.pbs.middleware.server.features.job.events.JobSubmittedEvent;
import com.pbs.middleware.server.features.ownership.service.OwnershipService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.features.ownership.domain.DomainType.DOWNLOAD;
import static com.pbs.middleware.server.features.ownership.domain.DomainType.JOB;
import static com.pbs.middleware.server.features.ownership.domain.DomainType.UPLOAD;

@Log4j2
@Component
@RequiredArgsConstructor
public class OwnershipListener {

    @NonNull
    private final OwnershipService ownerService;

    @EventListener
    public void onApplicationEvent(final JobSubmittedEvent event) {
        ownerService.create(event.getDomainId(), JOB);
    }

    @EventListener
    public void onApplicationEvent(final DownloadInitialized event) {
        ownerService.create(event.getDomainId(), DOWNLOAD);
    }

    @EventListener
    public void onApplicationEvent(final UploadInitialized event) {
        ownerService.create(event.getDomainId(), UPLOAD);
    }
}
