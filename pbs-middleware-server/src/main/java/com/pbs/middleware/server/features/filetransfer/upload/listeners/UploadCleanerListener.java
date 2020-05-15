package com.pbs.middleware.server.features.filetransfer.upload.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorageException;
import com.pbs.middleware.server.features.filetransfer.upload.domain.Upload;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploaded;
import com.pbs.middleware.server.features.filetransfer.upload.events.TemporaryFileDeleted;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadCancelled;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFailed;
import com.pbs.middleware.server.features.filetransfer.upload.factory.UploadFactory;
import com.pbs.middleware.server.features.filetransfer.upload.status.service.UploadStatusService;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState.REMOVED_FROM_TEMPORARY_STORAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadCleanerListener {

    @NonNull
    private final UploadStatusService uploadStatusService;

    @NonNull
    private final UploadPublisher publisher;

    @NonNull
    private final TemporaryStorage temporaryStorage;

    @NonNull
    private final UploadFactory factory;

    @EventListener
    public void onApplicationEvent(final FileUploaded event) throws MiddlewareException {
        Upload upload = factory.get(event.getDomainId());
        removeFile(upload, event.getFilename());
    }

    @EventListener
    public void onApplicationEvent(final UploadFailed event) throws MiddlewareException {
        startTempStorageCleaning(event.getDomainId());
    }

    @EventListener
    public void onApplicationEvent(final UploadCancelled event) throws MiddlewareException {
        uploadStatusService.delete(event.getDomainId());
        startTempStorageCleaning(event.getDomainId());
    }

    private void startTempStorageCleaning(UUID uploadId) {
        log.info("Temporary files cleaning!");
        Upload upload = factory.get(uploadId);
        upload.getFilenames()
                .stream()
                .filter(it -> upload.getFiles().get(it).getState() != REMOVED_FROM_TEMPORARY_STORAGE)
                .forEach(filename -> removeFile(upload, filename));
    }

    private void removeFile(Upload upload, String filename) {
        final String tempFileId = upload.getFiles().get(filename).getTempId();
        log.info("Removing temporary file {}", tempFileId);
        try {
            temporaryStorage.deleteById(upload.getId(), tempFileId);
            publisher.publish(new TemporaryFileDeleted(upload.getId(), tempFileId));
            log.info("Temporary file \"{}\"[{}] was removed!", filename, tempFileId);
        } catch (TemporaryStorageException e) {
            log.error("Removing temporary file[" + tempFileId + "] failed!", e);
        }
    }
}
