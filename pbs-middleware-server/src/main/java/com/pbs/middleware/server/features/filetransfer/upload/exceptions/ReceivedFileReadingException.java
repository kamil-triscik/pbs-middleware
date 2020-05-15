package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import static com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadErrorCode.UPLOAD__MULTIPART_READING_FAILED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public final class ReceivedFileReadingException extends MiddlewareException {

    public ReceivedFileReadingException(MultipartFile multipartFile, IOException e) {
        super(
                BAD_REQUEST,
                UPLOAD__MULTIPART_READING_FAILED,
                "Multipart file \"" + multipartFile.getOriginalFilename() + "\" reading error: " + e.getLocalizedMessage(),
                "file"
        );
    }

}
