package com.pbs.middleware.common.validations;

import com.pbs.middleware.common.pbs.Walltime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WallTimeValidator implements ConstraintValidator<ValidWalltime, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        try {
            Walltime.from(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
