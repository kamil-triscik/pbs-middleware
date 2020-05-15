package com.pbs.middleware.server.features.contact.service;

import com.pbs.middleware.api.contact.CreateContact;
import com.pbs.middleware.api.contact.Contact;
import com.pbs.middleware.api.contact.UpdateContact;
import com.pbs.middleware.server.features.contact.repository.ContactType;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public Contact mapToDto(com.pbs.middleware.server.features.contact.repository.Contact contact) {
        Contact dto = new Contact();

        dto.setId(contact.getId());
        dto.setDescription(contact.getDescription());
        dto.setEmail(contact.getEmail());
        dto.setContactTypes(contact.getContactTypes().stream().map(ContactType::toString).collect(Collectors.toSet()));

        return dto;
    }

    public com.pbs.middleware.server.features.contact.repository.Contact mapFromDto(CreateContact dto) {
        com.pbs.middleware.server.features.contact.repository.Contact contact = new com.pbs.middleware.server.features.contact.repository.Contact();

        contact.setDescription(dto.getDescription());
        contact.setEmail(dto.getEmail());
        contact.setContactTypes(dto.getContactTypes().stream().map(ContactType::valueOf).collect(Collectors.toSet()));

        return contact;
    }

    public com.pbs.middleware.server.features.contact.repository.Contact mapFromDto(UpdateContact dto) {
        return mapFromDto(null, dto);
    }

    public com.pbs.middleware.server.features.contact.repository.Contact mapFromDto(UUID id, UpdateContact dto) {
        com.pbs.middleware.server.features.contact.repository.Contact contact = new com.pbs.middleware.server.features.contact.repository.Contact();

        if (id != null) {
            contact.setId(id);
        }
        contact.setDescription(dto.getDescription());
        contact.setEmail(dto.getEmail());
        contact.setContactTypes(dto.getContactTypes().stream().map(ContactType::valueOf).collect(Collectors.toSet()));

        return contact;
    }

}
