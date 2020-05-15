package com.pbs.middleware.server.features.filetransfer.upload.domain;

import lombok.Data;

@Data
public class FileProperties {

    private String filename;

    private UploadFileState state;

    private String rename;

    private String tempId;

    private Boolean extract;

    private String secured;

    private Long size;

    private String hash;

    public FileProperties(String filename, UploadFileState state) {
        this.filename = filename;
        this.state = state;
    }
}
