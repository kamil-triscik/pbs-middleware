package com.pbs.middleware.server.features.filetransfer.download.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadError;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadInitialized;
import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloadRequested;
import com.pbs.middleware.server.features.filetransfer.download.events.FilesListFetched;
import com.pbs.middleware.server.features.filetransfer.download.factory.DownloadFactory;
import com.pbs.middleware.server.features.ssh.shell.Shell;
import com.pbs.middleware.server.features.ssh.shell.ShellException;
import com.pbs.middleware.server.features.ssh.shell.ShellFactory;
import java.io.File;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.common.utils.Optional.ofNullable;
import static java.lang.String.format;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Slf4j
@Component
@RequiredArgsConstructor
public class DownloadRequestedListener {

    private final static String FOLDER_EXISTS_CMD = "if [ -d %s ]; then echo 'true'; else echo 'false'; fi";

//    private final static String EMPTY_FOLDER_CMD = "ls %s | wc -l";

    private final static String FOLDER_FILE_LIST_CMD = "ls -p %s | grep -v /";

    @NonNull
    private final DownloadFactory downloadFactory;

    @NonNull
    private final DownloadPublisher downloadPublisher;

    @NonNull
    private final ShellFactory shellFactory;

    @EventListener
    public void onApplicationEvent(final DownloadInitialized event) throws MiddlewareException {
        new Thread(() -> processDownloadRequest(event)).start();
    }

    private void processDownloadRequest(DownloadInitialized downloadRequested) {
        Shell shell = shellFactory.get(downloadFactory.get(downloadRequested.getDomainId()).getConnection());
        String folder = getFolder(downloadRequested);

        try {
            if (incorrectFolder(shell, folder)) {
                downloadPublisher.publish(new DownloadError(downloadRequested.getDomainId(), "Provided folder \"" + folder + "\" not found!", null));
                downloadPublisher.publish(new DownloadFailed(downloadRequested.getDomainId()));
                return;
            }
        } catch (ShellException e) {
            // toto add logic to restart for job
            downloadPublisher.publish(new DownloadError(downloadRequested.getDomainId(),
                    "Provided folder \"" + folder + "\" existence check failed!",
                    getStackTrace(e)));
            downloadPublisher.publish(new DownloadFailed(downloadRequested.getDomainId()));
            return;
        }

        if (downloadRequested.getFiles() != null && !downloadRequested.getFiles().isEmpty()) {
            processFiles(folder, downloadRequested.getFiles(), downloadRequested.getDomainId());
            return;
        }

        try {
            processFiles(folder, fetchFolderFiles(shell, folder, downloadRequested.getDomainId()), downloadRequested.getDomainId());
        } catch (ShellException e) {
            downloadPublisher.publish(new DownloadError(downloadRequested.getDomainId(),
                    "Not possible to fetch folder files list!",
                    getStackTrace(e)));
            downloadPublisher.publish(new DownloadFailed(downloadRequested.getDomainId()));
        }

    }

    private void processFiles(String folder, Set<String> files, UUID uuid) {
        files.stream()
                .map(file -> folder + file)
                .forEach(file -> new Thread(() -> {
                    MDC.put("objectId", "download-" + uuid.toString());
                    downloadPublisher.publish(new FileDownloadRequested(uuid, file));
                }).start());
    }

    private boolean incorrectFolder(Shell shell, String folder) throws ShellException {
        if (folder.isEmpty()) {
            return false;
        }
        return "false".equals(shell.executeCommand(format(FOLDER_EXISTS_CMD, folder)).getStdout().toLowerCase().replace("\n", ""));
    }

    private String getFolder(DownloadInitialized downloadRequested) {
        return ofNullable(downloadRequested.getFolder())
                .filter(folder -> !folder.isBlank())
                .map(folder -> folder + (folder.endsWith(File.separator) ? "" : File.separator))
                .orElse("");
    }

    private Set<String> fetchFolderFiles(Shell shell, String folder, UUID uuid) throws ShellException {
        Set<String> files = Set.of(shell.executeCommand(format(FOLDER_FILE_LIST_CMD, folder)).getStdout().split("\n"));
        downloadPublisher.publish(new FilesListFetched(uuid, files));
        return files;
    }

}
