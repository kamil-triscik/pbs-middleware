package com.pbs.middleware.server.features.filetransfer.upload.notification;

import com.pbs.middleware.server.features.filetransfer.upload.domain.Upload;
import com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileProvided;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploadFailed;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploaded;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFailed;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFinished;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadInitialized;
import com.pbs.middleware.server.features.filetransfer.upload.utils.UploadVisitor;
import com.pbs.middleware.server.features.notification.email.configuration.TemplateType;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;

import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState.FAILED;
import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState.NOT_PROVIDED;
import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState.UPLOADED;
import static com.pbs.middleware.server.features.notification.email.configuration.TemplateType.UPLOAD_FAIL;
import static com.pbs.middleware.server.features.notification.email.configuration.TemplateType.UPLOAD_SUCCESS;
import static java.util.Optional.ofNullable;

class UploadEmailParamsFactory extends UploadVisitor {

    private UploadEmailParamsFactory() {
    }

    static UploadEmailParams getParams(Upload upload) {
        UploadEmailParamsFactory factory = new UploadEmailParamsFactory();
        upload.accept(factory);
        factory.setUploadId(upload.getId());

        return new UploadEmailParams(
                upload.getId(),
                factory.getRecipient(),
                factory.getSubject(),
                factory.getTemplateType(),
                factory.getParams(),
                factory.getExceptions());
    }

    private UUID uploadId;

    private Instant startTime = null;
    private Instant endTime = null;
    private String connection = null;
    private String destination = null;
    @Getter(AccessLevel.PRIVATE)
    private String recipient = null;
    private String description = null;

    private final Map<Integer, Map<String, Object>> files = new HashMap<>();
    private final Map<String, Map<String, Object>> filesCollection = new HashMap<>();
    private final Map<String, UploadFileState> fileStates = new HashMap<>();
    @Getter(AccessLevel.PRIVATE)
    private final Map<String, String> exceptions = new HashMap<>();
    private Long totalSize = 0L;

    @Override
    protected void uploadStarted(UploadInitialized event) {
        startTime = event.getInstant();
        destination = event.getDestination();
        description = event.getDescription();
        connection = event.getConnection();
        recipient = event.getEmail();
        event.getFiles().forEach(file -> {
            Map<String, Object> fileMap = new HashMap<>();
            fileMap.put("name", file.getFilename());
            fileStates.put("state", NOT_PROVIDED);
            files.put(files.size() + 1, fileMap);
            filesCollection.put(file.getFilename() + 1, fileMap);
        });
    }

    @Override
    protected void fileProvided(FileProvided event) {
        totalSize += event.getSize();
        Map<String, Object> fileMap = filesCollection.get(event.getFilename());
        fileMap.put("size", getFormattedSize(event.getSize()));
        fileMap.put("destination", Path.of(destination, event.getFilename()).toAbsolutePath().toString());
    }

    @Override
    protected void fileUploaded(FileUploaded event) {
        exceptions.remove(event.getFilename());
        fileStates.put(event.getFilename(), UPLOADED);
    }

    @Override
    protected void fileUploadFailed(FileUploadFailed event) {
        this.exceptions.put(event.getFilename(), event.getError());
        this.fileStates.replace(event.getFilename(), FAILED);
    }

    @Override
    protected void allFilesUploaded(UploadFinished event) {
        endTime = event.getInstant();
    }

    @Override
    protected void uploadCompleteFailed(UploadFailed event) {
        endTime = event.getInstant();
    }

    private void setUploadId(UUID uploadId) {
        this.uploadId = uploadId;
    }

    private Map<String, Object> getParams() {
        Map<String, Object> parameters = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.getDefault()).withZone(ZoneOffset.UTC);

        files.values().forEach(fileMap -> {
            fileMap.put("state", fileStates.get(fileMap.get("name")));
        });

        parameters.put("id", uploadId.toString());
        parameters.put("start", formatter.format(startTime));
        parameters.put("end", ofNullable(endTime).map(formatter::format).orElse("-"));
        parameters.put("connection", connection.toString());
        parameters.put("destination", destination);
        parameters.put("description", description);
        parameters.put("count", files.size());
        parameters.put("size", getFormattedSize(totalSize));
        parameters.put("files", files);

        return parameters;
    }

    private String getFormattedSize(Long fileSize) {
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1048576) {
            return (fileSize / 1024) + " kB";
        }
        return (fileSize / 1048576) + " MB";
    }

    public TemplateType getTemplateType() {
        return fileStates.values().stream().filter(FAILED::equals).findFirst()
                .map(it -> UPLOAD_FAIL)
                .orElse(UPLOAD_SUCCESS);
    }

    public String getSubject() {
        return fileStates.values().stream().filter(FAILED::equals).findFirst()
                .map(it -> "File upload failed")
                .orElse("Upload successfully finished");
    }
}
