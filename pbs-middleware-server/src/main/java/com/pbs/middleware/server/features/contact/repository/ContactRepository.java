package com.pbs.middleware.server.features.contact.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends CrudRepository<Contact, UUID> {

    Set<Contact> findAllByContactTypes(ContactType type);

    Optional<Contact> findByEmail(String email);

}
