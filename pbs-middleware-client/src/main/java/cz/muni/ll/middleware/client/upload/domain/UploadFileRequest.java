package cz.muni.ll.middleware.client.upload.domain;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadFileRequest {

    private final String filename;

    private final String rename;

    private final Boolean extract;

    private final String secured;

    private File file;

}
