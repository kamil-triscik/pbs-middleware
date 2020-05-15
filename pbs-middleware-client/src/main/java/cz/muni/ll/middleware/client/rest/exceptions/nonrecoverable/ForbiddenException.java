package cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable;

public class ForbiddenException extends NonRecoverableException {

    public ForbiddenException() {
        super("Access denied!");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
