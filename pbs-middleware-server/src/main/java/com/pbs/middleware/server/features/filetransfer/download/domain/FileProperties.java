package com.pbs.middleware.server.features.filetransfer.download.domain;

import lombok.Data;

@Data
public class FileProperties {

    private String filename;

    private DownloadFileState state;

    private String remotePath;

    private String tempId;

    private Long size = -1L;

    private String hash = "-";

    public FileProperties(String filename, DownloadFileState state, String remotePath) {
        this.filename = filename;
        this.state = state;
        this.remotePath = remotePath;
    }
}
