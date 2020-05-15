package com.pbs.middleware.server.features.validations;

import com.pbs.middleware.server.features.script.repository.ScriptRepository;
import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScriptExistsValidator implements ConstraintValidator<ScriptExists, String> {

    private final ScriptRepository repository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        try {
            UUID id = UUID.fromString(value);
            return repository.findByNameOrUuid(id, value).isPresent();
        } catch (IllegalArgumentException e) {
            return repository.findByName(value).isPresent();
        }
    }
}
