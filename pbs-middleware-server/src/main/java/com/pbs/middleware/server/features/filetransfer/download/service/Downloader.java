package com.pbs.middleware.server.features.filetransfer.download.service;

import com.pbs.middleware.server.common.storage.temporary.TemporaryEntity;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import com.pbs.middleware.server.features.connection.repository.Connection;
import com.pbs.middleware.server.features.connection.service.ConnectionService;
import com.pbs.middleware.server.features.filetransfer.download.domain.Download;
import com.pbs.middleware.server.features.filetransfer.download.exceptions.DownloadException;
import com.pbs.middleware.server.features.filetransfer.download.exceptions.DownloadFromServerException;
import com.pbs.middleware.server.features.ssh.scp.File;
import com.pbs.middleware.server.features.ssh.scp.SCPException;
import com.pbs.middleware.server.features.ssh.scp.SCPFactory;
import java.nio.file.Path;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class Downloader {

    private final static String FILE_EXISTS_CMD = "if [ -f %s ]; then echo 'true'; else echo 'false'; fi";

    @NonNull
    private final ConnectionService connectionService;

    @NonNull
    private final SCPFactory scpFactory;

    @NonNull
    private final TemporaryStorage temporaryStorage;

    public DownloadedFile downloadFile(Download download, String filename) throws DownloadException {
        Connection connection = connectionService.get(download.getConnection());
        try {
            log.info("Downloading file " + filename);
            File file =  scpFactory.get(connection).download(Path.of(filename));
            TemporaryEntity entity = saveToTempStorage(download.getId(), filename, file);
            return new DownloadedFile(filename, file.getSize(), DigestUtils.md5DigestAsHex(file.getData()), entity.getId());
        } catch (SCPException e) {
            log.error("Downloading file[" + filename + " failed!", e);
            throw new DownloadFromServerException(filename, e);
        }
    }

    private TemporaryEntity saveToTempStorage(UUID id, String filename, File file) {
        log.info("Saving files to local temporary storage");
        final String tempFileId = format("%s-%s", id, filename);
        TemporaryEntity entity = temporaryStorage.save(id, tempFileId, file.getData());
        log.info("File {} with size {}B saved into temporary storage as '{}'", filename, entity.getDataLength(), tempFileId);
        return entity;
    }


}
