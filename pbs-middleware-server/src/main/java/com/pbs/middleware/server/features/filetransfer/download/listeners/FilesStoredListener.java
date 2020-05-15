package com.pbs.middleware.server.features.filetransfer.download.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.filetransfer.download.domain.Download;
import com.pbs.middleware.server.features.filetransfer.download.domain.DownloadFileState;
import com.pbs.middleware.server.features.filetransfer.download.domain.FileProperties;
import com.pbs.middleware.server.features.filetransfer.download.events.AllFilesFetched;
import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloaded;
import com.pbs.middleware.server.features.filetransfer.download.factory.DownloadFactory;
import com.pbs.middleware.server.features.filetransfer.download.utils.DownloadVisitor;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilesStoredListener extends DownloadVisitor {

    @NonNull
    private final DownloadPublisher downloadPublisher;

    @NonNull
    private final DownloadFactory downloadFactory;

    @EventListener
    public void onApplicationEvent(final FileDownloaded event) throws MiddlewareException {
        Download download = downloadFactory.get(event.getDomainId());
        if (download.canContinue()) {
            Map<String, FileProperties> states = download.getFileProperties();
            if (states.values().stream().map(FileProperties::getState).allMatch(DownloadFileState.PREPARED::equals)) {
                downloadPublisher.publish(new AllFilesFetched(event.getDomainId()));
            }
        }
    }
}
