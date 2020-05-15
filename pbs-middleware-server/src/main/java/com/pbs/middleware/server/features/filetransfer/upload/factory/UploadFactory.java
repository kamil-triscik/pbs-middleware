package com.pbs.middleware.server.features.filetransfer.upload.factory;

import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.common.utils.Optional;
import com.pbs.middleware.server.features.filetransfer.upload.domain.Upload;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadEvent;
import com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadAlreadyExistsException;
import com.pbs.middleware.server.features.filetransfer.upload.exceptions.UploadNotFoundException;
import com.pbs.middleware.server.features.filetransfer.upload.listeners.UploadPublisher;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.pbs.middleware.server.common.utils.Optional.empty;
import static com.pbs.middleware.server.common.utils.Optional.of;

@Service
@RequiredArgsConstructor
public class UploadFactory {

    @NonNull
    private final EventRepository<UploadEvent, UUID> repository;

    @NonNull
    private final UploadPublisher publisher;

    /**
     * Loads a upload for given id and applies all stored events on it.
     */
    public Upload get(UUID id) {
        List<UploadEvent> events = repository.findAllByDomainId(id);

        if (events.isEmpty()) {
            throw new UploadNotFoundException(id);
        }

        Upload upload = new Upload(id, publisher);

        events.forEach(upload::apply);

        return upload;
    }

    /**
     * Loads a upload for given id and applies all stored events on it.
     */
    public Optional<Upload> find(UUID id) {
        List<UploadEvent> events = repository.findAllByDomainId(id);

        if (events.isEmpty()) {
            return empty();
        }

        Upload upload = new Upload(id, publisher);

        events.forEach(upload::apply);

        return of(upload);
    }

    public Upload create(UUID id) {
        if (!repository.findAllByDomainId(id).isEmpty()) {
            throw new UploadAlreadyExistsException(id);
        }

        return new Upload(id, publisher);
    }

}
