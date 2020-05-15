package com.pbs.middleware.server.features.filetransfer.upload.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileOptions {

    private String filename;

    private String rename;

    private Boolean extract;

    private String secured;

}
