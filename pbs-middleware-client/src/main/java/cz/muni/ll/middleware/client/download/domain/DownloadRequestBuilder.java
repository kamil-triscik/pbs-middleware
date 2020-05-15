package cz.muni.ll.middleware.client.download.domain;

import com.pbs.middleware.api.download.DownloadRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DownloadRequestBuilder {

    private String description = null;

    private String connection = null;

    private String folder = null;

    private final Set<String> files = new HashSet<>();

    private String email = null;

    private Boolean notify = false;

    private boolean remove = false;

    public DownloadRequestBuilder(UUID connection) {
        if (connection == null) {
            throw new IllegalArgumentException("connection can not be null");
        }
        init(connection.toString());
    }

    public DownloadRequestBuilder(String connection) {
        init(connection);
    }

    private void init(String connection) {
        if (connection == null) {
            throw new IllegalArgumentException("connection can not be null");
        }
        if (connection.isEmpty()) {
            throw new IllegalArgumentException("connection can not be empty");
        }
        this.connection = connection;
    }

    public DownloadRequestBuilder file(String file) {
        if (file == null) {
            throw new IllegalArgumentException("File can not be null");
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File name cna not be empty");
        }
        this.files.add(file);
        return this;
    }

    public DownloadRequestBuilder files(String... files) {
        files(Set.of(files));
        return this;
    }

    public DownloadRequestBuilder files(Set<String> files) {
        if (files == null) {
            throw new IllegalArgumentException("file not be null");
        }
        files.forEach(this::file);
        return this;
    }

    public DownloadRequestBuilder folder(String folder) {
        if (folder == null) {
            throw new IllegalArgumentException("folder can not be null");
        }
        if (folder.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("folder can not be empty string");
        }
        this.folder = folder;
        return this;
    }

    public DownloadRequestBuilder description(String description) {
        if (description == null) {
            throw new IllegalArgumentException("description can not be null");
        }
        if (description.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("description can not be empty string");
        }
        this.description = description;
        return this;
    }

    public DownloadRequestBuilder email(String email) {
        if (email.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("email can not be empty string");
        }
        this.email = email;
        return this;
    }

    public DownloadRequestBuilder notify(Boolean notify) {
        this.notify = notify;
        return this;
    }


    public DownloadRequestBuilder remove() {
        this.remove = true;
        return this;
    }

    public DownloadRequestBuilder remove(boolean remove) {
        this.remove = remove;
        return this;
    }

    public DownloadRequest build() {
        if (files.isEmpty() && (folder == null || folder.isBlank())) {
            throw new IllegalStateException("List of files or folder must provided!");
        }
        DownloadRequest request = new DownloadRequest();
        request.setDescription(description);
        request.setConnection(connection);
        request.setFolder(folder);
        request.setFiles(files);
        request.setEmail(email);
        request.setNotify(notify);
        request.setRemove(remove);
        return request;
    }

}
