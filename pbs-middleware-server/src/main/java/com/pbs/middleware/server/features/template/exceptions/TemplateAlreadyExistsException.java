package com.pbs.middleware.server.features.template.exceptions;

import com.pbs.middleware.server.common.exception.AlreadyExistsException;
import com.pbs.middleware.server.common.exception.CommonErrorCode;

public final class TemplateAlreadyExistsException extends AlreadyExistsException {

    public TemplateAlreadyExistsException(String templateId) {
        super("TemplateId with id " + templateId + " already exists!", "templateId", CommonErrorCode.ALREADY_EXISTS);
    }

}
