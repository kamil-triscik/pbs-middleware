package com.pbs.middleware.common.validations;

import java.util.Collection;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StringCollectionValidator implements ConstraintValidator<ValidStringCollection, Collection<String>> {

    @Override
    public boolean isValid(Collection<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.stream().filter(Objects::nonNull).noneMatch(String::isBlank);
    }
}
