package com.pbs.middleware.server.features.contact.validations;

import com.pbs.middleware.server.features.contact.repository.Contact;
import com.pbs.middleware.server.features.contact.repository.ContactRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Contact> {

    private final ContactRepository repository;

    @Override
    public boolean isValid(Contact value, ConstraintValidatorContext context) {
        if (value == null || value.getEmail() == null || value.getEmail().isBlank()) {
            return true;
        }
        if (value.getId() == null) {
            return repository.findByEmail(value.getEmail()).isEmpty();
        } else {
            return repository.findByEmail(value.getEmail())
                    .map(Contact::getId)
                    .map(id -> id.equals(value.getId()))
                    .orElse(true);
        }
    }
}
