package cz.muni.ll.middleware.client.rest.exceptions.recoverable;

import cz.muni.ll.middleware.client.rest.exceptions.LLMClientException;

public abstract class RecoverableException extends LLMClientException {
    public RecoverableException(String message) {
        super(message);
    }

    public RecoverableException(Throwable cause) {
        super(cause);
    }
}
