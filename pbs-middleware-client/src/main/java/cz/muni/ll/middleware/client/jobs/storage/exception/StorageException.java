package cz.muni.ll.middleware.client.jobs.storage.exception;

public abstract class StorageException extends RuntimeException {

    protected StorageException(Exception e) {
        super(e);
    }

    protected StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable e) {
        super(message, e);
    }

}
