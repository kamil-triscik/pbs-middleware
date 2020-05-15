package com.pbs.middleware.server.features.filetransfer.upload.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileProvided;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploadStarted;
import com.pbs.middleware.server.features.filetransfer.upload.status.repository.UploadFileStatus;
import com.pbs.middleware.server.features.filetransfer.upload.status.repository.UploadStatus;
import com.pbs.middleware.server.features.filetransfer.upload.status.service.UploadStatusService;
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
public class FileProvidedListener {

    @NonNull
    private final UploadPublisher publisher;

    @NonNull
    private final UploadStatusService uploadStatusService;

    @EventListener
    public void onApplicationEvent(final FileProvided fileProvided) throws MiddlewareException {
        UploadStatus uploadStatus = new UploadStatus(fileProvided.getDomainId(), new Date().getTime(), false, new HashSet<>());
        UploadFileStatus fileUploadStatus = new UploadFileStatus(fileProvided.getDomainId(), uploadStatus, fileProvided.getFilename(), null);
        uploadStatus.addFile(fileUploadStatus);
        uploadStatusService.create(fileUploadStatus);
        publisher.publish(new FileUploadStarted(fileProvided.getDomainId(), fileProvided.getFilename()));
    }
}
