package cz.muni.ll.middleware.client.rest.exceptions;

import cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable.NonRecoverableException;
import cz.muni.ll.middleware.client.rest.exceptions.recoverable.ConnectionException;
import feign.RetryableException;
import feign.Retryer;
import java.net.ConnectException;

public class LLMClientRetryer implements Retryer {

    @Override
    public void continueOrPropagate(RetryableException e) {
        if (e.getCause() instanceof ConnectException) {
            throw new ConnectionException(e.getCause());
        }
        throw new NonRecoverableException(e.getCause());
    }

    @Override
    public Retryer clone() {
        return this;
    }
}
