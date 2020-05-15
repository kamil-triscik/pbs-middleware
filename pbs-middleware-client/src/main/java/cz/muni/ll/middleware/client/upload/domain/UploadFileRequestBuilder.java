package cz.muni.ll.middleware.client.upload.domain;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadFileRequestBuilder {

    private String filename;

    // Not supported yet
    private final String rename = null;

    // Not supported yet
    private final Boolean extract = false;

    // Not supported yet
    private final String secured = null;

    private final File file;

    public UploadFileRequestBuilder(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File can not be null");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File " + file.getName() + " does not exists");
        }
        this.file = file;
        this.filename = this.file.getName();
    }


    public UploadFileRequestBuilder filename(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename can not be null");
        }
        if (filename.isBlank()) {
            throw new IllegalArgumentException("Filename can not be blank");
        }
        this.filename = filename;
        return this;
    }

    public UploadFileRequest build() {
        return new UploadFileRequest(filename, rename, extract, secured, file);
    }

}
