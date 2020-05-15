package com.pbs.middleware.server.features.script.validation;

import com.pbs.middleware.server.features.script.repository.Script;
import com.pbs.middleware.server.features.script.repository.ScriptRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScriptUniqueNameValidator implements ConstraintValidator<UniqueName, Script> {

    private final ScriptRepository repository;

    @Override
    public boolean isValid(Script value, ConstraintValidatorContext context) {
        if (value == null || value.getName() == null || value.getName().isBlank()) {
            return true;
        }
        if (value.getId() == null) {
            return repository.findByName(value.getName()).isEmpty();
        } else {
            return repository.findByName(value.getName())
                    .map(Script::getId)
                    .map(id -> id.equals(value.getId()))
                    .orElse(true);
        }
    }
}
