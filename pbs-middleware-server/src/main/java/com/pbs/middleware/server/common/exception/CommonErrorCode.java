package com.pbs.middleware.server.common.exception;

public enum CommonErrorCode implements ErrorCode {

    NOT_FOUND("hgh"),
    ALREADY_EXISTS("hgh");

    public final String description;


    CommonErrorCode(final String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
