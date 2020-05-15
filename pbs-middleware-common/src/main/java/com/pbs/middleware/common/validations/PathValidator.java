package com.pbs.middleware.common.validations;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PathValidator implements ConstraintValidator<ValidPath, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        try {
            Path.of(value);
            return true;
        } catch (InvalidPathException e) {
            return false;
        }
    }
}
