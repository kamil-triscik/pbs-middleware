package com.pbs.middleware.server.features.filetransfer.download.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.ByteArrayResource;

@Getter
@AllArgsConstructor
public class DownloadFile {

    private final String name;

    private final Long length;

    private final ByteArrayResource resource;

}
