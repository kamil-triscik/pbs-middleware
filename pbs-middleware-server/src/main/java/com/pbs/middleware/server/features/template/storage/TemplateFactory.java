package com.pbs.middleware.server.features.template.storage;

import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.common.utils.Optional;
import com.pbs.middleware.server.features.template.domain.Template;
import com.pbs.middleware.server.features.template.events.TemplateEvent;
import com.pbs.middleware.server.features.template.exceptions.TemplateAlreadyExistsException;
import com.pbs.middleware.server.features.template.exceptions.TemplateNotFoundException;
import com.pbs.middleware.server.features.template.listeners.TemplatePublisher;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import static com.pbs.middleware.server.common.utils.Optional.empty;
import static com.pbs.middleware.server.common.utils.Optional.of;

@Service
@RequiredArgsConstructor
public class TemplateFactory {

    @NonNull
    private final EventRepository<TemplateEvent, String> repository;

    @NonNull
    private final TemplatePublisher publisher;

    /**
     * Loads a template for given id and applies all stored events on it.
     */
    public Template get(String id) {
        List<TemplateEvent> events = repository.findAllByDomainId(id);

        if (events.isEmpty()) {
            throw new TemplateNotFoundException(id);
        }

        Template template = new Template(id, publisher);

        events.forEach(template::apply);

        return template;
    }

    /**
     * Loads a template for given id and applies all stored events on it.
     */
    public Optional<Template> find(String id) {
        List<TemplateEvent> events = repository.findAllByDomainId(id);

        if (events.isEmpty()) {
            return empty();
        }

        Template template = new Template(id, publisher);

        events.forEach(template::apply);

        return of(template);
    }

    public Template create(String id) {
        if (!repository.findAllByDomainId(id).isEmpty()) {
            throw new TemplateAlreadyExistsException(id);
        }

        return new Template(id, publisher);
    }

}
