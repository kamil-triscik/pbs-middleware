package cz.muni.ll.middleware.client.rest.exceptions.recoverable;

public class ServerException extends RecoverableException {
    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }
}
