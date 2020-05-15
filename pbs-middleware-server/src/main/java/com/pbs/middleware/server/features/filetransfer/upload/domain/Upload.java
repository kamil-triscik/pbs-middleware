package com.pbs.middleware.server.features.filetransfer.upload.domain;

import com.pbs.middleware.server.common.domain.MiddlewareEventObject;
import com.pbs.middleware.server.common.domain.Visitable;
import com.pbs.middleware.server.common.domain.Visitor;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileProvided;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploadStarted;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploaded;
import com.pbs.middleware.server.features.filetransfer.upload.events.TemporaryFileDeleted;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadCancelled;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadEvent;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFailed;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadInitialized;
import com.pbs.middleware.server.features.filetransfer.upload.listeners.UploadPublisher;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState.CANCELLED;
import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState.NOT_PROVIDED;
import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState.PROVIDED;
import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState.REMOVED_FROM_TEMPORARY_STORAGE;
import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState.UPLOADED;
import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState.UPLOADING;
import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState.INITIALIZED;
import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState.IN_PROGRESS;
import static com.pbs.middleware.server.features.filetransfer.upload.exceptions.InvalidUploadActionException.invalidCancelAction;

@RequiredArgsConstructor
public final class Upload extends MiddlewareEventObject<UploadEvent> implements Visitable<UploadEvent> {

    @NonNull
    @Getter
    private final UUID id;

    @NonNull
    private final UploadPublisher publisher;

    @Getter
    private final Queue<UploadEvent> events = new ConcurrentLinkedQueue<>();

    @Getter
    private volatile UploadState state = INITIALIZED;

    @Getter
    private String destination;

    @Getter
    private String description;

    @Getter
    private String connection;

    @Getter
    private String email;

    @Getter
    private final Set<String> filenames = new HashSet<>();

    @Getter
    final Map<String, String> temporaryFileIds = new HashMap<>();

    @Getter
    final Map<String, FileProperties> files = new HashMap<>();

    public void accept(Visitor<UploadEvent> visitor) {
        events.forEach(visitor::visit);
    }

    public void start(String destination, String connection, String email, String description, List<FileOptions> files) {
        publisher.publishAsync(new UploadInitialized(getId(), destination, connection, email, description, files));
    }

    public void uploadFile(String filename, Long size, String hash, String tempId) {
        publisher.publish(new FileProvided(this.getId(), filename, size, hash, tempId));
    }

    public void cancel(String reason, Boolean removeUploaded) {
        if (Stream.of(INITIALIZED, IN_PROGRESS).noneMatch(it -> it.equals(state))) {
            throw invalidCancelAction(state);
        }
        publisher.publishAsync(new UploadCancelled(this.id, reason, removeUploaded));
    }

    @Override
    public void apply(UploadEvent event) {
        events.add(event);
        if (event instanceof UploadInitialized) {
            this.uploadInitialized((UploadInitialized) event);
        } else if (event instanceof FileProvided) {
            this.fileProvided((FileProvided) event);
        } else if (event instanceof FileUploaded) {
            this.fileUploaded((FileUploaded) event);
        } else if (event instanceof UploadFailed) {
            this.uploadFailed((UploadFailed) event);
        } else if (event instanceof TemporaryFileDeleted) {
            this.fileRemoved((TemporaryFileDeleted) event);
        } else if (event instanceof FileUploadStarted) {
            this.fileUploadStarted((FileUploadStarted) event);
        } else if (event instanceof UploadCancelled) {
            this.uploadCancelled((UploadCancelled) event);
        }
    }

    private void uploadInitialized(UploadInitialized event) {
        this.connection = event.getConnection();
        this.destination = event.getDestination();
        this.description = event.getDescription();
        this.email = event.getEmail();
        event.getFiles().stream().map(FileOptions::getFilename).forEach(filename -> {
            filenames.add(filename);
            files.put(filename, new FileProperties(filename, NOT_PROVIDED));
        });
        event.getFiles().forEach(options -> {
            FileProperties properties = this.files.get(options.getFilename());
            properties.setRename(properties.getRename());
            properties.setExtract(properties.getExtract());
            properties.setSecured(properties.getSecured());
        });
    }

    private void fileProvided(FileProvided event) {
        FileProperties properties = this.files.get(event.getFilename());
        properties.setState(PROVIDED);
        properties.setSize(event.getSize());
        properties.setHash(event.getHash());
        properties.setTempId(event.getTempId());
        temporaryFileIds.put(event.getTempId(), event.getFilename());
    }

    private void fileUploaded(FileUploaded event) {
        FileProperties properties = this.files.get(event.getFilename());
        properties.setState(UPLOADED);
        this.state = files.values().stream().map(FileProperties::getState).allMatch(UPLOADED::equals) ? UploadState.UPLOADED : IN_PROGRESS;
    }

    private void fileRemoved(TemporaryFileDeleted event) {
        FileProperties properties = this.files.get(temporaryFileIds.get(event.getTempId()));
        properties.setState(REMOVED_FROM_TEMPORARY_STORAGE);
    }

    private void fileUploadStarted(FileUploadStarted fileUploadStarted) {
        this.state = IN_PROGRESS;
        FileProperties properties = this.files.get(fileUploadStarted.getFilename());
        properties.setState(UPLOADING);
    }

    private void uploadFailed(UploadFailed event) {
        this.state = UploadState.FAILED;
    }

    private void uploadCancelled(UploadCancelled event) {
        this.state = UploadState.CANCELLED;
        if (event.getRemoveUploadedFiles()) {
            this.files.values().forEach(file -> file.setState(CANCELLED));
        } else {
            this.getFilenames()
                    .stream()
                    .filter(file -> files.get(file).getState() != UPLOADED)
                    .forEach(file -> files.get(file).setState(CANCELLED));
        }
    }

}
