package cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable;

public class NotFoundException extends NonRecoverableException {

    public NotFoundException(String message) {
        super(message);
    }
}
