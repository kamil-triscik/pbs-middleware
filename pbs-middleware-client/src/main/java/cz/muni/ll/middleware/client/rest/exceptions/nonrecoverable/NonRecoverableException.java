package cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable;

import cz.muni.ll.middleware.client.rest.exceptions.LLMClientException;


public class NonRecoverableException extends LLMClientException {
    public NonRecoverableException(String message) {
        super(message);
    }

    public NonRecoverableException(Throwable cause) {
        super(cause);
    }
}
