package com.pbs.middleware.common.validations;

import java.util.Collection;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CollectionValidator implements ConstraintValidator<ValidCollection, Collection<?>> {

    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.stream().noneMatch(Objects::isNull);
    }
}
