package com.pbs.middleware.api.error;

public enum CommonErrorCode implements ErrorCode {

    INTERNAL_ERROR(""),
    UNAUTHORIZED(""),
    VALIDATION_ERROR(""),
    INVALID_REQUEST_BODY(""),
    INVALID_REQUEST_METHOD("");

    private final String description;

    CommonErrorCode(final String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

}
