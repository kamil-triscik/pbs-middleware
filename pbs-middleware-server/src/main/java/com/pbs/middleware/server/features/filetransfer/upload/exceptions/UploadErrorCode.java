package com.pbs.middleware.server.features.filetransfer.upload.exceptions;

import com.pbs.middleware.server.common.exception.ErrorCode;

public enum UploadErrorCode implements ErrorCode {

    // TODO: 11.10.20 provide correct error description 
    UPLOAD__ALREADY_EXISTS("Upload with provided id already exist."),
    UPLOAD__NOT_FOUND("Job with provided id does not exists."),
    UPLOAD__LOADING_EXCEPTION("Job loading from DB problem."),

    UPLOAD__TEMP_FILE_NOT_FOUND("File not found on temporary storage"),
    UPLOAD__TEMP_FILE_LOAD_FAILED("File not found on temporary storage"),

    UPLOAD__UNEXPECTED_FILENAME("Unexpected filename. File with such name wasn't expected!"),

    UPLOAD__CONNECTION_NOT_FOUND("File not found on temporary storage"),
    UPLOAD__INVALID_EMAIL("File not found on temporary storage"),
    UPLOAD__MULTIPART_READING_FAILED("File not found on temporary storage"),
    UPLOAD__INVALID_UPLOAD_DESTINATION("File not found on temporary storage"),
    UPLOAD__MISSING_UPLOAD_DESTINATION("File not found on temporary storage"),

    UPLOAD__REMOTE_UPLOAD_FAILED("File not found on temporary storage"),
    UPLOAD__REMOTE_DEST_PREP_FAILED("Destination directory preparation failed"),

    UPLOAD__FORBIDDEN_CONFIRM_ACTION("File not found on temporary storage"),
    UPLOAD__FORBIDDEN_CANCEL_ACTION("File not found on temporary storage"),
    UPLOAD__FORBIDDEN_DELETE_ACTION("File not found on temporary storage"),
    UPLOAD__FORBIDDEN_USER_UPLOAD_ACTION("File not found on temporary storage");

    public final String description;

    UploadErrorCode(final String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
