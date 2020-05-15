package cz.muni.ll.middleware.client.jobs.storage.exception;

public class TableCreationFailedException extends StorageException {

    public TableCreationFailedException(Exception e) {
        super(e);
    }
}
