package cz.muni.ll.middleware.client.jobs.storage.exception;

import java.nio.file.Path;

import static java.lang.String.format;

public class AlreadyExistsException extends StorageException {

    public AlreadyExistsException(Path path) {
        super(format("Job file [%s] already exists", path.getFileName().toString()));
    }
}
