package cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable;

import cz.muni.ll.middleware.client.rest.exceptions.LLMClientException;

public class MethodNotAllowedException extends LLMClientException {
    public MethodNotAllowedException(String message) {
        super(message);
    }

    public MethodNotAllowedException(Throwable cause) {
        super(cause);
    }
}
