package com.pbs.middleware.server.features.validations;

import com.pbs.middleware.server.features.template.exceptions.TemplateNotFoundException;
import com.pbs.middleware.server.features.template.storage.TemplateFactory;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TemplateExistsValidator implements ConstraintValidator<TemplateExists, String> {

    private final TemplateFactory factory;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        try {
            factory.get(value);
            return true;
        } catch (TemplateNotFoundException e) {
            return false;
        }
    }
}
