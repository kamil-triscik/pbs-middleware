package cz.muni.ll.middleware.client.jobs.storage.exception;

public class DatabaseConnectionFailedException extends StorageException {

    public DatabaseConnectionFailedException(Exception e) {
        super(e);
    }
}
