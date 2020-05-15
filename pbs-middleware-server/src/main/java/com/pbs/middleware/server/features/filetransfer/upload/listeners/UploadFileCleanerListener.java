package com.pbs.middleware.server.features.filetransfer.upload.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.filetransfer.upload.domain.Upload;
import com.pbs.middleware.server.features.filetransfer.upload.events.RemoteFileDeleted;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadCancelled;
import com.pbs.middleware.server.features.filetransfer.upload.factory.UploadFactory;
import com.pbs.middleware.server.features.ssh.shell.ShellException;
import com.pbs.middleware.server.features.ssh.shell.ShellFactory;
import com.pbs.middleware.server.features.ssh.shell.ShellUtils;
import java.nio.file.Path;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadFileCleanerListener {

    @NonNull
    private final UploadPublisher publisher;

    @NonNull
    private final ShellFactory shellFactory;

    @NonNull
    private final UploadFactory factory;

    @EventListener
    public void onApplicationEvent(final UploadCancelled event) throws MiddlewareException {
        if (event.getRemoveUploadedFiles()) {
            startRemoteCleaning(event);
        }
    }

    private void startRemoteCleaning(final UploadCancelled event) {
        log.info("Removing already uploaded files from target server");
        Upload upload = factory.get(event.getDomainId());
        final String destination = upload.getDestination();
        final String connection = upload.getConnection();
        upload.getFilenames().parallelStream().forEach(filename ->
            removeFile(event.getDomainId(), filename, destination, connection)
        );
    }

    private void removeFile(UUID domainId, String filename, String destination, String connection) {
        try {
            final Path remotePath = Path.of(destination, filename);
            final String removeCmd = ShellUtils.removeFileCmd(remotePath);
            shellFactory.get(connection).executeCommand(removeCmd); // TODO: 11.10.20 check result
            publisher.publish(new RemoteFileDeleted(domainId, filename, remotePath.toString()));
        } catch (ShellException e) {
            log.error("Removing already uploaded files from target server failed", e);
        }
    }
}
