package com.pbs.middleware.server.features.connection.validations;

import java.nio.file.Files;
import java.nio.file.Path;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SshKeyExistenceValidator implements ConstraintValidator<SshKeyExists, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        try {
            return Files.exists(Path.of(value));
        } catch (Exception e) {
            log.error("Ssh key validation exception", e);
            return false;
        }
    }
}
