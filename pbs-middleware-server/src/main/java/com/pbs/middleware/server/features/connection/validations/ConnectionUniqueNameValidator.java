package com.pbs.middleware.server.features.connection.validations;

import com.pbs.middleware.server.features.connection.repository.Connection;
import com.pbs.middleware.server.features.connection.repository.ConnectionRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConnectionUniqueNameValidator implements ConstraintValidator<UniqueName, Connection> {

    private final ConnectionRepository repository;

    @Override
    public boolean isValid(Connection value, ConstraintValidatorContext context) {
        if (value == null || value.getName() == null || value.getName().isBlank()) {
            return true;
        }
        if (value.getId() == null) {
            return repository.findByName(value.getName()).isEmpty();
        } else {
            return repository.findByName(value.getName())
                    .map(Connection::getId)
                    .map(id -> id.equals(value.getId()))
                    .orElse(true);
        }

    }
}
