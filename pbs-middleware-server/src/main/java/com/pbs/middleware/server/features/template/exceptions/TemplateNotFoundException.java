package com.pbs.middleware.server.features.template.exceptions;

import com.pbs.middleware.server.common.exception.NotFoundException;

import static com.pbs.middleware.server.common.exception.CommonErrorCode.NOT_FOUND;

public final class TemplateNotFoundException extends NotFoundException {

    public TemplateNotFoundException(String templateId) {
        super("Template with id " + templateId + " not found!", "templateId", NOT_FOUND);
    }

}
