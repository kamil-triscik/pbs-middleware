package cz.muni.ll.middleware.client.jobs.storage.exception;

public class LoadingException extends StorageException {

    public LoadingException(String message) {
        super(message);
    }

    public LoadingException(String message, Throwable e) {
        super(message, e);
    }
}
