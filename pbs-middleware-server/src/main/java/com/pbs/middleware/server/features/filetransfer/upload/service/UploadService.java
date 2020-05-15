package com.pbs.middleware.server.features.filetransfer.upload.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.api.upload.UploadFileRequest;
import com.pbs.middleware.server.common.domain.Id;
import com.pbs.middleware.server.common.security.SecurityUser;
import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.common.validations.NotEmptyString;
import com.pbs.middleware.common.validations.ValidCollection;
import com.pbs.middleware.common.validations.ValidPath;
import com.pbs.middleware.server.common.storage.temporary.TemporaryEntity;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import com.pbs.middleware.server.features.filetransfer.upload.domain.FileOptions;
import com.pbs.middleware.server.features.filetransfer.upload.domain.Upload;
import com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadEvent;
import com.pbs.middleware.server.features.filetransfer.upload.exceptions.UnexpectedFileException;
import com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadNotFoundException;
import com.pbs.middleware.server.features.filetransfer.upload.factory.UploadFactory;
import com.pbs.middleware.server.features.ownership.service.OwnershipService;
import com.pbs.middleware.server.features.validations.ConnectionExists;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState.INITIALIZED;
import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState.IN_PROGRESS;
import static com.pbs.middleware.server.features.filetransfer.upload.exceptions.InvalidUploadActionException.invalidDeleteAction;
import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @NonNull
    private final UploadFactory uploadFactory;

    @NonNull
    private final EventRepository<UploadEvent, UUID> repository;

    @NonNull
    private final OwnershipService ownershipService;

    @NonNull
    private final TemporaryStorage temporaryStorage;

    public Set<UUID> getUploads() {
        return repository.findAllIds().stream().map(Id::getDomainId).collect(Collectors.toSet());
    }

    public void upload(
            @NotNull UUID id,
            @NotBlank @ConnectionExists String connection,
            @NotBlank(message = "destination is mandatory")
            @ValidPath(fieldName = "destination") String destination,
            @Email @NotEmptyString String email,
            Boolean notify,
            String description,
            @NotEmpty(message = "list of upload files can not be empty")
            @ValidCollection(fieldName = "files") List<UploadFileRequest> files) {
        List<FileOptions> fileOptions = files
                .stream()
                .map(request -> new FileOptions(
                        request.getFilename(),
                        request.getRename(),
                        request.getExtract(),
                        request.getSecured()))
                .collect(Collectors.toList());
        uploadFactory.create(id).start(destination, connection, getEmail(email, notify), description, fileOptions);
    }

    public void uploadFile(UUID id, byte[] data, String filename) {
        Upload upload = uploadFactory.get(id);
        if (!upload.getFilenames().contains(filename)) {
            throw new UnexpectedFileException(id, filename);
        }
        log.info("Saving files to local temporary storage");
        final String tempFileId = format("%s-%s", id, filename);
        TemporaryEntity entity = temporaryStorage.save(id, tempFileId, data);
        log.info("File {} with size {}B saved into temporary storage as '{}'", filename, entity.getDataLength(), tempFileId);

        upload.uploadFile(filename, (long) data.length, DigestUtils.md5DigestAsHex(data), tempFileId);
    }

    public Upload get(UUID id) {
        return uploadFactory.get(id);
    }

    public Map<String, String> getEvents(UUID id) {
        Map<String, String> events = new LinkedHashMap<>();
        repository.findAllByDomainId(id)
                .forEach(event -> events.put(event.getClass().getSimpleName(), eventToString(event)));
        if (events.isEmpty()) {
            throw new UploadNotFoundException(id);
        }
        return events;
    }

    public void cancel(UUID id, String reason, Boolean removeUploaded) {
        uploadFactory.get(id).cancel(reason, removeUploaded);
    }

    public void delete(UUID domainId) {
        UploadState state = uploadFactory.get(domainId).getState();
        if (state == INITIALIZED || state == IN_PROGRESS) {
            throw invalidDeleteAction(state);
        }
        repository.deleteAllByDomainId(domainId);
        ownershipService.delete(domainId);
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

    private String eventToString(UploadEvent event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Event deserialization failed", e);
            return "serialization failed";
        }
    }
}
