package cz.muni.ll.middleware.client.jobs.storage.exception;

public class DeleteFailedException extends StorageException {

    public DeleteFailedException(Exception e) {
        super(e);
    }

    public DeleteFailedException(String message) {
        super(message);
    }
}
