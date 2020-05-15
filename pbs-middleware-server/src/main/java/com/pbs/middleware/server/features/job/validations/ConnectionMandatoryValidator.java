package com.pbs.middleware.server.features.job.validations;

import com.pbs.middleware.api.job.JobSubmit;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConnectionMandatoryValidator implements ConstraintValidator<ConnectionMandatory, JobSubmit> {

    @Override
    public boolean isValid(JobSubmit value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value.getTemplateId() == null || value.getTemplateId().isBlank()) {
            return value.getConnection() != null && !value.getConnection().isBlank();
        }
        return true;
    }
}
