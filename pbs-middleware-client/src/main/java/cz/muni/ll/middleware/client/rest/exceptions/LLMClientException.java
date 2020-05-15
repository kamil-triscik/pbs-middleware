package cz.muni.ll.middleware.client.rest.exceptions;

public class LLMClientException extends RuntimeException {

    public LLMClientException(String message) {
        super(message);
    }

    public LLMClientException(Throwable cause) {
        super(cause);
    }
}
