package cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable;

public class NonAcceptableRequestException extends NonRecoverableException {

    public NonAcceptableRequestException(String message) {
        super(message);
    }
}
