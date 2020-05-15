package com.pbs.middleware.server.features.filetransfer.download.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.nio.file.Paths;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DownloadedFile {

    private final String path;

    private final Long size;

    private final String hash;

    private final String tempId;

    @JsonIgnore
    public String getName() {
        return Paths.get(this.getPath()).getFileName().toString();
    }

}
