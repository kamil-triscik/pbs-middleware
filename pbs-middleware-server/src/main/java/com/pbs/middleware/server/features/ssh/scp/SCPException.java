package com.pbs.middleware.server.features.ssh.scp;

public class SCPException extends Exception {

    public SCPException(String message) {
        super(message);
    }

    public SCPException(String message, Throwable cause){
        super(message, cause);
    }

}
