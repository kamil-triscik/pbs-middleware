package com.pbs.middleware.server.features.filetransfer.download.service;

import com.pbs.middleware.api.download.Download;
import com.pbs.middleware.api.download.DownloadFileState;
import com.pbs.middleware.api.download.DownloadState;
import com.pbs.middleware.api.download.File;
import com.pbs.middleware.server.features.filetransfer.download.utils.DownloadVisitor;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.api.download.DownloadState.UNKNOWN;
import static java.util.stream.Collectors.toList;

@Component
public class DownloadMapper extends DownloadVisitor {

    public Download toDto(com.pbs.middleware.server.features.filetransfer.download.domain.Download download) {

        Download dto = new Download();

        dto.setId(download.getId());
        dto.setState(getState(download.getState()));
        dto.setConnection(download.getConnection());
        dto.setFolder(download.getFolder());
        dto.setDescription(download.getDescription());

        dto.setFiles(download.getFileProperties()
                .values()
                .stream()
                .map(props -> File
                        .builder()
                        .path(props.getRemotePath())
                        .size(props.getSize())
                        .hash(props.getHash())
                        .state(getState(props.getState()))
                        .build())
                .collect(toList()));

        return dto;
    }

    private DownloadState getState(com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState state) {
        return switch (state) {
            case REQUESTED, IN_PROGRESS -> DownloadState.IN_PROGRESS;
            case READY -> DownloadState.READY;
            case DOWNLOADED, POST_PROCESSING, FINISHED, FINISHED_WITH_WARNING -> DownloadState.FINISHED;
            case CANCELED -> DownloadState.CANCELED;
            case FAILED -> DownloadState.FAILED;
            default -> UNKNOWN;
        };
    }

    private DownloadFileState getState(com.pbs.middleware.server.features.filetransfer.download.domain.DownloadFileState state) {
        return switch (state) {
            case IN_PROGRESS -> DownloadFileState.IN_PROGRESS;
            case PREPARED -> DownloadFileState.PREPARED;
            case FAILED -> DownloadFileState.FAILED;
            case REMOVED_FROM_TEMPORARY_STORE -> DownloadFileState.FINISHED;
            default -> DownloadFileState.UNKNOWN;
        };
    }

}
