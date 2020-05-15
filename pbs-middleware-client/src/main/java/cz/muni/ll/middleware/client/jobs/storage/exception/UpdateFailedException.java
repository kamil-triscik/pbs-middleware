package cz.muni.ll.middleware.client.jobs.storage.exception;

public class UpdateFailedException extends StorageException {

    public UpdateFailedException(Exception e) {
        super(e);
    }
}
