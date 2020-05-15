package com.pbs.middleware.server.features.filetransfer.upload.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFinished;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploaded;
import com.pbs.middleware.server.features.filetransfer.upload.factory.UploadFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState.UPLOADED;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadedListener {

    @NonNull
    private final UploadFactory uploadFactory;

    @NonNull
    private final UploadPublisher publisher;

    @EventListener
    public void onEvent(final FileUploaded event) throws MiddlewareException {
        if (!UPLOADED.equals(uploadFactory.get(event.getDomainId()).getState())) {
            return;
        }
        log.info("All files were uploaded!");
        publisher.publish(new UploadFinished(event.getDomainId()));
    }
}
