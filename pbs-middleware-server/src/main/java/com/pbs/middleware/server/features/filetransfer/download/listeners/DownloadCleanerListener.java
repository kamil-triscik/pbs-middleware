package com.pbs.middleware.server.features.filetransfer.download.listeners;

import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorageException;
import com.pbs.middleware.server.features.filetransfer.download.domain.Download;
import com.pbs.middleware.server.features.filetransfer.download.domain.DownloadFileState;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadCancelled;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadEvent;
import com.pbs.middleware.server.features.filetransfer.download.events.FileRemoved;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingLaunched;
import com.pbs.middleware.server.features.filetransfer.download.factory.DownloadFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Slf4j
@Component
@RequiredArgsConstructor
public class DownloadCleanerListener {

    @NonNull
    private final DownloadFactory downloadFactory;

    @NonNull
    private final DownloadPublisher downloadPublisher;

    @NonNull
    private final TemporaryStorage temporaryStorage;

    @EventListener
    public void onApplicationEvent(final PostProcessingLaunched event) {
        cleanTemporaryFiles(event);
    }

    @EventListener
    public void onApplicationEvent(final DownloadCancelled event) {
        cleanTemporaryFiles(event);
    }

    private void cleanTemporaryFiles(DownloadEvent event) {
        log.info("Temporary files cleaning!");
        Download download = downloadFactory.get(event.getDomainId());
        download.getFileProperties()
                .values()
                .stream()
                .filter(props -> props.getState() != DownloadFileState.REMOVED_FROM_TEMPORARY_STORE)
                .forEach(props -> {
                    final String tempFileId = props.getTempId();
                    final String fileName = props.getFilename();
                    try {
                        log.info("Removing temporary file {}!", tempFileId);
                        temporaryStorage.deleteById(event.getDomainId(), tempFileId);
                        log.debug("Temporary file \"" + fileName + "\"[" + tempFileId + "] was removed!");
                    } catch (TemporaryStorageException e) {
                        log.error("Removing temporary file[" + tempFileId + "] failed!", e);
                        downloadPublisher.publish(new PostProcessingFailed(event.getDomainId(), "Removing file from temp storage failed!", getStackTrace(e)));
                    }
                    downloadPublisher.publish(new FileRemoved(event.getDomainId(), fileName, tempFileId));
                });
    }

}
