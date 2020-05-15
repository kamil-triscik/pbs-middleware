package cz.muni.ll.middleware.client.jobs.storage.exception;

import static java.lang.String.format;

public class NotFoundException extends StorageException {

    public NotFoundException(String id) {
        super(format("Job file for id %s not found", id));
    }
}
