package com.pbs.middleware.server.features.filetransfer.download.listeners;

import com.pbs.middleware.server.features.filetransfer.download.domain.Download;
import com.pbs.middleware.server.features.filetransfer.download.domain.FileProperties;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadEvent;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingFinished;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingLaunched;
import com.pbs.middleware.server.features.filetransfer.download.factory.DownloadFactory;
import com.pbs.middleware.server.features.ssh.shell.ShellException;
import com.pbs.middleware.server.features.ssh.shell.ShellFactory;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostProcessingLaunchedListener {

    @NonNull
    private final DownloadFactory downloadFactory;

    @NonNull
    private final DownloadPublisher downloadPublisher;

    @NonNull
    private final ShellFactory shellFactory;

    @EventListener
    public void onApplicationEvent(final PostProcessingLaunched event) {
        Download download = downloadFactory.get(event.getDomainId());
        DownloadEvent toNotify = new PostProcessingFinished(event.getDomainId());
        if (download.getRemove()) {
            String command = download.getFileProperties().values().stream()
                    .map(FileProperties::getRemotePath)
                    .map(filePath -> "rm " + filePath)
                    .collect(Collectors.joining(","));
            try {
                log.debug("Removing remote files. Command: {}", command);
                String response = shellFactory.get(download.getConnection()).executeCommand(command).getStderr(); //todo
                if (response != null && !response.isBlank()) {
                    toNotify = new PostProcessingFailed(event.getDomainId(),
                            "Removing remote files probably failed!",
                            "<b>Command:</b>" + command + "<br><b>Output</b>:" + response);
                }
            } catch (ShellException e) {
                toNotify = new PostProcessingFailed(event.getDomainId(), "Removing remote files failed.", getStackTrace(e));
            }
        }
        downloadPublisher.publish(toNotify);
    }
}
