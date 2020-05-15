package com.pbs.middleware.server.features.filetransfer.download.factory;

import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.common.utils.Optional;
import com.pbs.middleware.server.features.filetransfer.download.domain.Download;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadEvent;
import com.pbs.middleware.server.features.filetransfer.download.exceptions.DownloadAlreadyExistsException;
import com.pbs.middleware.server.features.filetransfer.download.exceptions.DownloadNotFoundException;
import com.pbs.middleware.server.features.filetransfer.download.listeners.DownloadPublisher;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.pbs.middleware.server.common.utils.Optional.empty;
import static com.pbs.middleware.server.common.utils.Optional.of;

@Service
@RequiredArgsConstructor
public class DownloadFactory {

    @NonNull
    private final EventRepository<DownloadEvent, UUID> repository;

    @NonNull
    private final DownloadPublisher publisher;

    /**
     * Loads a download for given id and applies all stored events on it.
     */
    public Download get(UUID id) {
        List<DownloadEvent> events = repository.findAllByDomainId(id);

        if (events.isEmpty()) {
            throw new DownloadNotFoundException(id);
        }

        Download download = new Download(id, publisher);

        events.forEach(download::apply);

        return download;
    }

    /**
     * Loads a download for given id and applies all stored events on it.
     */
    public Optional<Download> find(UUID id) {
        List<DownloadEvent> events = repository.findAllByDomainId(id);

        if (events.isEmpty()) {
            return empty();
        }

        Download download = new Download(id, publisher);

        events.forEach(download::apply);

        return of(download);
    }

    public Download create(UUID id) {
        if (!repository.findAllByDomainId(id).isEmpty()) {
            throw new DownloadAlreadyExistsException(id);
        }

        return new Download(id, publisher);
    }

}
