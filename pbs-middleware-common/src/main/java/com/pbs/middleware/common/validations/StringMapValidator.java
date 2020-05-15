package com.pbs.middleware.common.validations;

import java.util.Map;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class StringMapValidator implements ConstraintValidator<ValidStringMap, Map<String, String>> {

    @Override
    public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.keySet().stream().filter(Objects::nonNull).noneMatch(StringUtils::isBlank)
                && value.values().stream().filter(Objects::nonNull).noneMatch(StringUtils::isBlank);
    }
}
