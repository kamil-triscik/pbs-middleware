package cz.muni.ll.middleware.client.jobs.storage.exception;

public class DatabaseCreationFailedException extends StorageException {

    public DatabaseCreationFailedException(Exception e) {
        super(e);
    }

    public DatabaseCreationFailedException(String message) {
        super(message);
    }
}
