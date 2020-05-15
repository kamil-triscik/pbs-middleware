package com.pbs.middleware.server.features.filetransfer.download.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DownloadFile {

    private String path;

    private Long size;

    private DownloadFileState state;

}