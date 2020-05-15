package com.pbs.middleware.server.features.filetransfer.download.exceptions;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class FileNotFoundException extends DownloadException {

    public FileNotFoundException(String filename) {
        super(
                NOT_FOUND,
                DownloadErrorCode.DOWNLOAD__TEMP_FILE_NOT_FOUND,
                format("File %s not found!", filename));
    }
}
