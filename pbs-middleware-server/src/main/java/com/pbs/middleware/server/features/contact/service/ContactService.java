package com.pbs.middleware.server.features.contact.service;

import com.google.common.collect.ImmutableSet;
import com.pbs.middleware.server.features.contact.repository.Contact;
import com.pbs.middleware.server.features.contact.repository.ContactRepository;
import com.pbs.middleware.server.features.contact.repository.ContactType;
import com.pbs.middleware.server.features.users.repository.User;
import com.pbs.middleware.server.features.users.service.UserService;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class ContactService {

    @NonNull
    private final ContactRepository contactRepository;

    @NonNull
    private final UserService userService;

    public Set<Contact> getAll() {
        return ImmutableSet.copyOf(contactRepository.findAll());
    }

    public Contact get(@NonNull UUID uuid) {
        return contactRepository.findById(uuid).orElseThrow(() -> ContactNotFoundException.of(uuid));
    }

    public Set<Contact> findDtoByType(@NonNull String contactType) {
        validateType(contactType);
        return findByType(ContactType.valueOf(contactType));
    }

    public Set<Contact> findByType(@NonNull ContactType contactType) {
        return contactRepository.findAllByContactTypes(contactType);
    }

    public Contact create(@Valid @NonNull Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact update(@NonNull UUID id, @Valid @NonNull Contact contact) {
        get(id);
        return contactRepository.save(contact);
    }

    public Set<String> getAdminEmails() {
        return userService.getAdmins().stream().map(User::getEmail).collect(Collectors.toSet());
    }

    public void delete(@NonNull UUID id) {
        contactRepository.deleteById(id);
    }

    private void validateType(String type) {
        try {
            ContactType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw WrongTypeException.of(type);
        }
    }

}
