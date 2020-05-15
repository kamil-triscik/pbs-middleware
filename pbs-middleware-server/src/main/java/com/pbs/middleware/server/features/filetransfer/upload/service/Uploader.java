package com.pbs.middleware.server.features.filetransfer.upload.service;

import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import com.pbs.middleware.server.features.connection.repository.Connection;
import com.pbs.middleware.server.features.connection.service.ConnectionService;
import com.pbs.middleware.server.features.filetransfer.upload.domain.Upload;
import com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadException;
import com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadToServerException;
import com.pbs.middleware.server.features.ssh.scp.SCPException;
import com.pbs.middleware.server.features.ssh.scp.SCPFactory;
import com.pbs.middleware.server.features.ssh.shell.Result;
import com.pbs.middleware.server.features.ssh.shell.ShellException;
import com.pbs.middleware.server.features.ssh.shell.ShellFactory;
import java.nio.file.Path;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadErrorCode.UPLOAD__REMOTE_DEST_PREP_FAILED;
import static com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadErrorCode.UPLOAD__REMOTE_UPLOAD_FAILED;
import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class Uploader {

    @NonNull
    private final ConnectionService connectionService;

    @NonNull
    private final SCPFactory scpFactory;

    @NonNull
    private final ShellFactory shellFactory;

    @NonNull
    private final TemporaryStorage temporaryStorage;

    public void uploadFile(Upload upload, String fileName, String tempFileId) throws UploadException {
        String newFilename = upload.getFiles().get(fileName).getRename();
        Path destination = Path.of(upload.getDestination(), newFilename != null && !newFilename.isBlank() ? newFilename : fileName);
        Connection connection = connectionService.get(upload.getConnection());
        try {
            if (upload.getDestination() != null) {
                log.info("Preparing destination directory");
                Result result = shellFactory.get(connection).executeCommand("mkdir -p " + upload.getDestination());
                if (result.getStderr() != null && !result.getStderr().isEmpty()) {
                    throw new IllegalStateException(format("Destination preparations failed: %s", result.getStderr()));
                    // TODO: 30.03.20 add exit code checking
                }
                log.info("Uploading file " + fileName + (newFilename != null && !newFilename.isBlank() ? " renamed to " + newFilename : ""));
                scpFactory.get(connection).upload(temporaryStorage.get(upload.getId(), tempFileId).getData(), destination);
            } else {
                log.warn("Missing destination property!");
            }
        } catch (ShellException e) {
            log.error(format("Preparing destination directory[%s] failed!", upload.getDestination()), e);
            throw new UploadException(UPLOAD__REMOTE_DEST_PREP_FAILED.description, UPLOAD__REMOTE_DEST_PREP_FAILED, e);
        } catch (SCPException e) {
            log.error("Uploading file[" + tempFileId + "] to " + destination.toAbsolutePath() + " failed!", e);
            throw new UploadToServerException(tempFileId, destination.toAbsolutePath().toString(), e);
        } catch (Exception e) {
            log.error("Unexpected upload exception!", e);
            throw new UploadException(UPLOAD__REMOTE_UPLOAD_FAILED.description, UPLOAD__REMOTE_UPLOAD_FAILED, e);
        }
    }
}
