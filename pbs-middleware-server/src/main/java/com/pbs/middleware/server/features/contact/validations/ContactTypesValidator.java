package com.pbs.middleware.server.features.contact.validations;

import com.pbs.middleware.server.features.contact.repository.ContactType;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactTypesValidator implements ConstraintValidator<ValidContactTypes, Set<String>> {

    @Override
    public boolean isValid(Set<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.stream().noneMatch(type -> {
            try {
                ContactType.valueOf(type);
                return false;
            } catch (IllegalArgumentException e) {
                return true;
            }
        });
    }
}