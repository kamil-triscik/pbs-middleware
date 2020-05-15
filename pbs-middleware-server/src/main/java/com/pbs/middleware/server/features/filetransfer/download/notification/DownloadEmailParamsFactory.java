package com.pbs.middleware.server.features.filetransfer.download.notification;

import com.pbs.middleware.server.features.filetransfer.download.domain.Download;
import com.pbs.middleware.server.features.filetransfer.download.domain.DownloadFileState;
import com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState;
import com.pbs.middleware.server.features.filetransfer.download.events.AllFilesFetched;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadError;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadInitialized;
import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloaded;
import com.pbs.middleware.server.features.filetransfer.download.events.FileFetchingFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.FilesListFetched;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingFailed;
import com.pbs.middleware.server.features.filetransfer.download.utils.DownloadVisitor;
import com.pbs.middleware.server.features.notification.email.configuration.TemplateType;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;

import static com.pbs.middleware.server.features.notification.email.configuration.TemplateType.DOWNLOAD_FAIL;
import static com.pbs.middleware.server.features.notification.email.configuration.TemplateType.DOWNLOAD_SUCCESS;
import static java.util.Optional.ofNullable;

class DownloadEmailParamsFactory extends DownloadVisitor {

    private DownloadEmailParamsFactory() {
    }

    static DownloadEmailParams getParams(Download download) {
        DownloadEmailParamsFactory factory = new DownloadEmailParamsFactory();
        download.accept(factory);
        factory.setUploadId(download.getId());

        return new DownloadEmailParams(
                download.getId(),
                factory.getRecipient(),
                factory.getSubject(),
                factory.getTemplateType(download),
                factory.getParams(),
                factory.getExceptions());
    }

    private UUID downloadId;

    private Instant startTime = null;
    private Instant endTime = null;
    private String connection = null;
    private String source = null;
    @Getter(AccessLevel.PRIVATE)
    private String recipient = null;
    private String description = null;

    private Map<String, Map<String, Object>> files = new HashMap<>();
    private Map<String, DownloadFileState> fileStates = new HashMap<>();
    @Getter(AccessLevel.PRIVATE)
    private Map<String, String> exceptions = new HashMap<>();
    private String errorMessage = null;
    private String error = null;
    private Long totalSize = 0L;

    private void setUploadId(UUID downloadId) {
        this.downloadId = downloadId;
    }

    private Map<String, Object> getParams() {
        Map<String, Object> parameters = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.getDefault()).withZone(ZoneOffset.UTC);

        files.values().forEach(fileMap -> {
            fileMap.put("state", fileStates.get(fileMap.get("name")));
        });

        Map<Integer, Object> intFiles = new LinkedHashMap<>();
        files.values().forEach(it -> intFiles.put(intFiles.size() + 1, it));

        parameters.put("id", downloadId.toString());
        parameters.put("start", formatter.format(startTime));
        parameters.put("end", ofNullable(endTime).map(formatter::format).orElse("-"));
        parameters.put("connection", connection.toString());
        parameters.put("source", source);
        parameters.put("description", description);
        parameters.put("count", files.size());
        parameters.put("size", getFormattedSize(totalSize));
        parameters.put("files", intFiles);
        parameters.put("error", error);
        parameters.put("errorMessage", errorMessage);

        return parameters;
    }

    @Override
    protected void apply(DownloadInitialized event) {
        startTime = event.getInstant();
        source = event.getFolder() != null ? event.getFolder() : "./";
        description = event.getDescription();
        connection = event.getConnection();
        recipient = event.getEmail();
        if (event.getFiles() != null) {
            initFilesMap(event.getFiles());
        }
    }

    @Override
    protected void apply(FilesListFetched event) {
        initFilesMap(event.getFiles());
    }

    @Override
    protected void apply(FileDownloaded event) {
        totalSize += event.getSize();
        exceptions.remove(event.getFilename());
        fileStates.put(event.getFilename(), DownloadFileState.PREPARED);
        files.get(event.getFilename()).put("size", getFormattedSize(event.getSize()));
    }

    @Override
    protected void apply(FileFetchingFailed event) {
        this.exceptions.put(event.getFilename(), event.getException());
        this.fileStates.replace(event.getEventType(), DownloadFileState.FAILED);
    }

    @Override
    protected void apply(AllFilesFetched event) {
        this.endTime = event.getInstant();
    }

    @Override
    protected void apply(DownloadError event) {
        this.errorMessage = event.getMessage();
        this.error = event.getException();
        this.exceptions.put(error, event.getException());
    }

    @Override
    protected void apply(PostProcessingFailed event) {
        this.errorMessage = event.getMessage();
        this.error = event.getException();
        this.exceptions.put(error, event.getException());
    }

    private void initFilesMap(Set<String> filenames) {
        filenames.forEach(filename -> {
            Map<String, Object> fileMap = new HashMap<>();
            fileMap.put("name", filename);
            fileMap.put("size", "-");
            fileMap.put("source", Path.of(source, filename).toString());
            fileStates.put(filename, DownloadFileState.UNKNOWN);
            files.put(filename, fileMap);
        });
    }

    private String getFormattedSize(Long fileSize) {
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1048576) {
            return (fileSize / 1024) + " kB";
        }
        return (fileSize / 1048576) + " MB";
    }

    private TemplateType getTemplateType(Download download) {
        return DownloadState.READY.equals(download.getState()) ? DOWNLOAD_SUCCESS: DOWNLOAD_FAIL;
    }

    private String getSubject() {
        return fileStates.values().stream().filter(DownloadFileState.FAILED::equals).findFirst()
                .map(it -> "File download failed")
                .orElse("Files prepared for download");
    }
}
