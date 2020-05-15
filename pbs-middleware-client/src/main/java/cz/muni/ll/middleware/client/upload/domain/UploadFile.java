package cz.muni.ll.middleware.client.upload.domain;

import lombok.Data;

@Data
public class UploadFile {

    private final String filePath;

    private final UploadFileState state;

}
