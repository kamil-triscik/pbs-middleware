package com.pbs.middleware.server.features.filetransfer.download.listeners;

import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloadRequested;
import com.pbs.middleware.server.features.filetransfer.download.factory.DownloadFactory;
import com.pbs.middleware.server.features.filetransfer.download.status.repository.DownloadStatus;
import com.pbs.middleware.server.features.filetransfer.download.status.repository.FileDownloadStatus;
import com.pbs.middleware.server.features.filetransfer.download.status.service.DownloadStatusService;
import java.util.Date;
import java.util.HashSet;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileDownloadRequestedListener {

    @NonNull
    private final DownloadFactory downloadFactory;

    @NonNull
    private final DownloadStatusService downloadStatusService;

    @EventListener
    public void onApplicationEvent(final FileDownloadRequested event) {
        if (downloadFactory.get(event.getDomainId()).canContinue()) {
            DownloadStatus downloadStatus = new DownloadStatus(event.getDomainId(), new Date().getTime(), false, new HashSet<>());
            FileDownloadStatus fileDownloadStatus = new FileDownloadStatus(null, downloadStatus, event.getFilename(), null);
            downloadStatus.addFile(fileDownloadStatus);
            downloadStatusService.create(fileDownloadStatus);
        }
    }

}
