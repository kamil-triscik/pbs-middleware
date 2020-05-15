package cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable;

public class BadRequestException extends NonRecoverableException {

    public BadRequestException() {
        super("Access denied!");
    }

    public BadRequestException(String message) {
        super(message);
    }
}
