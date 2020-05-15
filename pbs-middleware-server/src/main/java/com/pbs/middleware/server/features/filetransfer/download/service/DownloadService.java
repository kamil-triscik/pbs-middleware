package com.pbs.middleware.server.features.filetransfer.download.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.server.common.domain.Id;
import com.pbs.middleware.server.common.security.SecurityUser;
import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.common.storage.temporary.TemporaryEntity;
import com.pbs.middleware.server.common.storage.temporary.TemporaryFileNotFoundException;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorageException;
import com.pbs.middleware.server.features.filetransfer.download.domain.Download;
import com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState;
import com.pbs.middleware.server.features.filetransfer.download.domain.FileProperties;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadEvent;
import com.pbs.middleware.server.features.filetransfer.download.exceptions.DownloadNotFoundException;
import com.pbs.middleware.server.features.filetransfer.download.exceptions.FileNotFoundException;
import com.pbs.middleware.server.features.filetransfer.download.exceptions.InvalidDownloadActionException;
import com.pbs.middleware.server.features.filetransfer.download.exceptions.UserFileDownloadException;
import com.pbs.middleware.server.features.filetransfer.download.factory.DownloadFactory;
import com.pbs.middleware.server.features.validations.ConnectionExists;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class DownloadService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @NonNull
    private final DownloadFactory factory;

    @NonNull
    private final EventRepository<DownloadEvent, UUID> repository;

    @NonNull
    private final TemporaryStorage temporaryStorage;

    public Set<UUID> getDownloads() {
        return repository.findAllIds().stream().map(Id::getDomainId).collect(Collectors.toSet());
    }

    public void download(@NonNull UUID id,
                         @ConnectionExists @NotNull(message = "Connection is mandatory2") String connection,
                         @Email String email,
                         Boolean notify,
                         @NotNull String folder,
                         String description,
                         @NotNull Set<String> files,
                         @NotNull Boolean isRemove) {
        //    @ConnectionExists
        //@ValidDownloadRequest
        factory.create(id).startDownload(
                connection,
                getEmail(email, notify),
                folder,
                description,
                files,
                isRemove);
    }

    public Download get(UUID id) {
        return factory.get(id);
    }

    public Map<String, String> getEvents(UUID id) {
        Map<String, String> events = new LinkedHashMap<>();
        repository.findAllByDomainId(id)
                .forEach(event -> events.put(event.getClass().getSimpleName(), eventToString(event)));
        if (events.isEmpty()) {
            throw new DownloadNotFoundException(id);
        }
        return events;
    }

    public void confirm(UUID id) {
        factory.get(id).confirm();
    }

    public void cancel(UUID id, String reason) {
        factory.get(id).cancel(reason);
    }

    public void delete(UUID domainId) {
        repository.deleteAllByDomainId(domainId);
//        ownerService.delete(domainId);
    }

    public DownloadFile getFile(UUID id, String filename) {
        Download download = factory.get(id);
        readyForDownload(download);

        String tempFileId = download.getFileProperties().get(filename).getTempId();
        if (tempFileId == null) {
            throw new FileNotFoundException(filename);
        }

        try {
            TemporaryEntity entity = temporaryStorage.get(id, tempFileId);
            return new DownloadFile(filename, entity.getDataLength(), new ByteArrayResource(entity.getData()));
        } catch (TemporaryStorageException e) {
            throw new FileNotFoundException(filename);
        }
    }

    public DownloadFile getFiles(UUID id) {
        Download download = factory.get(id);
        readyForDownload(download);

        Map<String, FileProperties> files = download.getFileProperties();

        File zip = null;
        String zipName = id + "-files.zip";
        try {

            zip = File.createTempFile(id + "-files", "zip");
            FileOutputStream fos = new FileOutputStream(zipName);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (String filename : files.keySet()) {
                ByteArrayInputStream fis = new ByteArrayInputStream(temporaryStorage.get(id, files.get(filename).getTempId()).getData());
                ZipEntry zipEntry = new ZipEntry(filename);
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            fos.close();
            return new DownloadFile(zipName, zip.length(), new ByteArrayResource(Files.readAllBytes(zip.toPath())));
        } catch (TemporaryFileNotFoundException e) {
            throw new FileNotFoundException("filename");
        } catch (Exception e) {
            throw new UserFileDownloadException(e.getMessage());
        } finally {
            if (zip != null) {
                if (zip.delete()) {
                    log.warn("Zip file {} deletion failed!", zipName);
                }
            }
        }
    }

    private String getEmail(String email, Boolean notify) {
        if (notify == null || !notify) {
            return null;
        }
        if (email == null || email.isBlank()) {
            return ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        }
        return email;
    }

    private void readyForDownload(Download download) {
        if (!DownloadState.READY.equals(download.getState())) {
            throw InvalidDownloadActionException.dataNotPrepared();
        }
    }

    private String eventToString(DownloadEvent event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Event deserialization failed", e);
            return "serialization failed";
        }
    }
}
