package cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable;

public class ConflictException extends NonRecoverableException {

    public ConflictException(String message) {
        super(message);
    }
}
