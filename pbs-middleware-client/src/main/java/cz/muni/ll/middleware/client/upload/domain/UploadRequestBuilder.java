package cz.muni.ll.middleware.client.upload.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UploadRequestBuilder {

    private UUID id = null;

    private String connection = null;

    private String destination = null;

    private String email = null;

    private Boolean notify = null;

    private String description = null;

    private List<UploadFileRequest> files = new ArrayList<>();

    public UploadRequestBuilder(UUID connection, String destination) {
        if (connection == null) {
            throw new IllegalArgumentException("connection can not be null");
        }
        init(connection.toString(), destination, Collections.emptyList());
    }

    public UploadRequestBuilder(UUID connection, String destination, UploadFileRequest file) {
        if (connection == null) {
            throw new IllegalArgumentException("connection can not be null");
        }
        init(connection.toString(), destination, file);
    }

    public UploadRequestBuilder(String connection, String destination, List<UploadFileRequest> file) {
        init(connection, destination, file);
    }

    public UploadRequestBuilder(String connection, String destination, UploadFileRequest... files) {
        init(connection, destination, Arrays.asList(files));
    }

    private void init(String connection, String destination, UploadFileRequest file) {
        init(connection, destination, new ArrayList<>() {{
            add(file);
        }});
    }

    private void init(String connection, String destination, List<UploadFileRequest> files) {
        if (connection == null) {
            throw new IllegalArgumentException("connection can not be null");
        }
        if (connection.isEmpty()) {
            throw new IllegalArgumentException("connection can not be empty");
        }
        this.connection = connection;
        if (destination == null) {
            throw new IllegalArgumentException("destination can not be null");
        }
        if (destination.isEmpty()) {
            throw new IllegalArgumentException("destination can not be empty");
        }
        this.destination = destination;
        if (files == null) {
            throw new IllegalArgumentException("file not be null");
        }
        files.forEach(file -> {
            if (file == null) {
                throw new IllegalArgumentException("File can not be null");
            }
            if (!file.getFile().exists()) {
                throw new IllegalArgumentException("File " + file.getFilename() + " does not exists");
            }
        });
        this.files.addAll(files);
    }

    public UploadRequestBuilder id(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID can not be null");
        }

        this.id = id;
        return this;
    }

    public UploadRequestBuilder destination(String destination) {
        if (destination == null) {
            throw new IllegalArgumentException("destination can not be null");
        }
        if (destination.isEmpty()) {
            throw new IllegalArgumentException("destination can not be empty");
        }
        this.destination = destination;
        return this;
    }

    public UploadRequestBuilder email(String email) {
        if (email == null) {
            throw new IllegalArgumentException("email can not be null");
        }
        if (email.isEmpty()) {
            throw new IllegalArgumentException("email can not be empty");
        }
        this.email = email;
        return this;
    }

    public UploadRequestBuilder notify(Boolean notify) {
        this.notify = notify;
        return this;
    }

    public UploadRequestBuilder description(String description) {
        if (description == null) {
            throw new IllegalArgumentException("description can not be null");
        }
        if (description.isEmpty()) {
            throw new IllegalArgumentException("description can not be empty");
        }
        this.description = description;
        return this;
    }

    public UploadRequestBuilder file(UploadFileRequest uploadFileRequest) {
        if (uploadFileRequest == null) {
            throw new IllegalArgumentException("uploadFileRequest can not be null");
        }
        this.getFiles().add(uploadFileRequest);
        return this;
    }

    public FileBuilder buildFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file can not be null");
        }
        return new FileBuilder(this, file);
    }

    public UploadRequest build() {
        if (files.isEmpty()) {
            throw new IllegalStateException("Upload request can not contain empty lost of files!");
        }
        return new UploadRequest(id, connection, destination, email, notify, description, files);
    }

    public static class FileBuilder {

        private final UploadRequestBuilder builder;

        private final UploadFileRequestBuilder fileRequestBuilder;

        private FileBuilder(UploadRequestBuilder builder, File file) {
            this.builder = builder;
            this.fileRequestBuilder = new UploadFileRequestBuilder(file);
        }

        public FileBuilder filename(String filename) {
            fileRequestBuilder.filename(filename);
            return this;
        }

        public UploadRequestBuilder build() {
            UploadFileRequest request = fileRequestBuilder.build();
            builder.getFiles().add(request);
            return builder;
        }

    }

}
