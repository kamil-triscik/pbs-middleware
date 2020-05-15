package com.pbs.middleware.server.features.filetransfer.upload.service;

import com.pbs.middleware.api.upload.UploadState;
import com.pbs.middleware.api.upload.Upload;
import com.pbs.middleware.api.upload.File;
import com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UploadMapper {

    public Upload toDto(com.pbs.middleware.server.features.filetransfer.upload.domain.Upload upload) {
        Upload dto = new Upload();

        dto.setId(upload.getId());
        dto.setConnection(upload.getConnection());
        dto.setDestination(upload.getDestination());
        dto.setDescription(upload.getDescription());
        dto.setFiles(filesToDto(upload));
        dto.setState(getState(upload.getState()));

        return dto;
    }

    private List<File> filesToDto(com.pbs.middleware.server.features.filetransfer.upload.domain.Upload upload) {
        return upload
                .getFilenames()
                .stream()
                .map(filename -> new File(
                        filename,
                        upload.getFiles().get(filename).getSize(),
                        upload.getFiles().get(filename).getHash(),
                        UploadFileState.get(upload.getFiles().get(filename).getState())
                ))
                .collect(Collectors.toList());
    }

    private UploadState getState(com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState state) {
        return switch (state) {
            case INITIALIZED -> UploadState.QUEUED;
            case IN_PROGRESS -> UploadState.IN_PROGRESS;
            case UPLOADED -> UploadState.UPLOADED;
            case FAILED -> UploadState.FAILED;
            case CANCELLED -> UploadState.CANCELLED;
            default -> UploadState.UNKNOWN;
        };
    }

}
