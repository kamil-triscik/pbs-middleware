package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState;

import static org.springframework.http.HttpStatus.FORBIDDEN;


public class InvalidUploadActionException extends UploadException {

    private InvalidUploadActionException(UploadErrorCode errorCode, String message) {
        super(FORBIDDEN, errorCode, message);
    }

    public static InvalidUploadActionException invalidConfirmAction(UploadState state) {
        return new InvalidUploadActionException(UploadErrorCode.UPLOAD__FORBIDDEN_CONFIRM_ACTION, "Confirm action is forbidden for upload process in state " + state);
    }

    public static InvalidUploadActionException invalidCancelAction(UploadState state) {
        return new InvalidUploadActionException(UploadErrorCode.UPLOAD__FORBIDDEN_CANCEL_ACTION, "Cancel action is forbidden for upload process in state " + state);
    }

    public static InvalidUploadActionException invalidDeleteAction(UploadState state) {
        return new InvalidUploadActionException(UploadErrorCode.UPLOAD__FORBIDDEN_DELETE_ACTION, "Delete action is forbidden for upload process in state " + state);
    }

    public static InvalidUploadActionException dataNotPrepared() {
        return new InvalidUploadActionException(UploadErrorCode.UPLOAD__FORBIDDEN_USER_UPLOAD_ACTION, "File upload is enabled only for upload process in state FETCHED");
    }

}