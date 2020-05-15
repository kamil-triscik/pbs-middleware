package com.pbs.middleware.server.features.notification.email.service;

import com.pbs.middleware.server.features.contact.repository.Contact;
import com.pbs.middleware.server.features.contact.repository.ContactType;
import com.pbs.middleware.server.features.contact.service.ContactService;
import com.pbs.middleware.server.features.job.email.domain.EmailEntity;
import com.pbs.middleware.server.features.job.email.repository.EmailRepository;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.features.job.email.domain.EmailState.WAITING;
import static java.util.stream.Collectors.toSet;

@Log
@Component
@RequiredArgsConstructor
public class EmailNotifier {

    @NonNull
    private final EmailRepository emailRepository;

    @NonNull
    private final ContactService contactService;

    public void send(String email, String subject,  String text) {
        store(getEmail(email), subject, text);
    }

    public void sendJobNotification(String subject, String text) {
        log.warning("Sending job notification");
        Set<String> contacts =  contactService.findByType(ContactType.JOB).stream().map(Contact::getEmail).collect(toSet());
        if (contacts.isEmpty()) {
            log.warning("Not job contacts");
            contacts = contactService.getAdminEmails();
        }
        if (contacts.isEmpty()) {
            log.warning("No contacts for job notification!");
            return;
        }
        store(contacts, subject, text);
    }

    private String getEmail(String email) {
        try {
            UUID contact = UUID.fromString(email);
            return contactService.get(contact).getEmail();
        } catch (Exception e) {
            return email;
        }
    }
    private void store(Set<String> emails, String subject, String body) {

        store(StringUtils.join(emails, ","), subject, body);
    }

    private void store(String email, String subject, String body) {
        EmailEntity entity = new EmailEntity();

        entity.setRecipient(email);
        entity.setSubject(subject);
        entity.setBody(body);
        entity.setState(WAITING);
        entity.setCreateDate(new Date().getTime());
        emailRepository.save(entity);
    }
}
