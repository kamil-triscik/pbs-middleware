package com.pbs.middleware.common.validations;

import java.util.Map;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapValidator implements ConstraintValidator<ValidMap, Map<?, ?>> {

    @Override
    public boolean isValid(Map<?, ?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.keySet().stream().noneMatch(Objects::isNull)
                && value.values().stream().noneMatch(Objects::isNull);
    }
}
