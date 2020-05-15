package cz.muni.ll.middleware.client.jobs.storage.exception;

public class SaveFailedException extends StorageException {

    public SaveFailedException(Exception e) {
        super(e);
    }
}
