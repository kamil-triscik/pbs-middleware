package cz.muni.ll.middleware.client.rest.exceptions.recoverable;

final public class ConnectionException extends RecoverableException {
    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(Throwable cause) {
        super(cause);
    }
}
