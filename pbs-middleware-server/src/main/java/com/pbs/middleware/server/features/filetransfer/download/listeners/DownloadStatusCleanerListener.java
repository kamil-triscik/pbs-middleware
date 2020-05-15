package com.pbs.middleware.server.features.filetransfer.download.listeners;

import com.pbs.middleware.server.features.filetransfer.download.events.DownloadCancelled;
import com.pbs.middleware.server.features.filetransfer.download.status.service.DownloadStatusService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DownloadStatusCleanerListener {

    @NonNull
    private final DownloadStatusService downloadStatusService;

    @EventListener
    public void onApplicationEvent(final DownloadCancelled event) {
        log.info("Removing download status with id {} from DB", event.getDomainId());
        downloadStatusService.delete(event.getDomainId());
    }

}
