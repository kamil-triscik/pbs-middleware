package com.pbs.middleware.server.features.filetransfer.download.validations;

import com.pbs.middleware.api.download.DownloadRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("PMD.UselessParentheses")
@RequiredArgsConstructor
public class DownloadRequestValidator implements ConstraintValidator<ValidDownloadRequest, DownloadRequest> {

    @Override
    public boolean isValid(DownloadRequest value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !((value.getFolder() == null || value.getFolder().isBlank()) && (value.getFiles().isEmpty()));
    }
}
