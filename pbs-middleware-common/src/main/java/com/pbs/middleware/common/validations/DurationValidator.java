package com.pbs.middleware.common.validations;

import java.time.Duration;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DurationValidator implements ConstraintValidator<ValidDuration, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            Duration.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
