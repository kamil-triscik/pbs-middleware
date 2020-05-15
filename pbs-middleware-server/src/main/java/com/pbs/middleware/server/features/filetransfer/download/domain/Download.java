package com.pbs.middleware.server.features.filetransfer.download.domain;

import com.pbs.middleware.server.common.domain.MiddlewareEventObject;
import com.pbs.middleware.server.common.domain.Visitable;
import com.pbs.middleware.server.common.domain.Visitor;
import com.pbs.middleware.server.features.filetransfer.download.events.AllFilesFetched;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadCancelled;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadConfirmed;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadEvent;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadInitialized;
import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloadRequested;
import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloaded;
import com.pbs.middleware.server.features.filetransfer.download.events.FileRemoved;
import com.pbs.middleware.server.features.filetransfer.download.events.FilesListFetched;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingFinished;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingLaunched;
import com.pbs.middleware.server.features.filetransfer.download.listeners.DownloadPublisher;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.pbs.middleware.server.features.filetransfer.download.domain.DownloadFileState.PREPARED;
import static com.pbs.middleware.server.features.filetransfer.download.domain.DownloadFileState.REMOVED_FROM_TEMPORARY_STORE;
import static com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState.CANCELED;
import static com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState.DOWNLOADED;
import static com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState.FAILED;
import static com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState.FINISHED;
import static com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState.FINISHED_WITH_WARNING;
import static com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState.IN_PROGRESS;
import static com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState.POST_PROCESSING;
import static com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState.READY;
import static com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState.REQUESTED;
import static com.pbs.middleware.server.features.filetransfer.download.domain.DownloadState.UNKNOWN;
import static com.pbs.middleware.server.features.filetransfer.download.exceptions.InvalidDownloadActionException.invalidCancelAction;
import static com.pbs.middleware.server.features.filetransfer.download.exceptions.InvalidDownloadActionException.invalidConfirmAction;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
public final class Download extends MiddlewareEventObject<DownloadEvent> implements Visitable<DownloadEvent> {

    @NonNull
    @Getter
    private final UUID id;

    private final DownloadPublisher publisher;

    @Getter
    private final Queue<DownloadEvent> events = new ConcurrentLinkedQueue<>();

    @Getter
    private volatile DownloadState state = UNKNOWN;

    @Getter
    private String connection;

    @Getter
    private String email;

    @Getter
    private String description;

    @Getter
    private String folder;

    @Getter
    private Set<String> files;

    @Getter
    private Boolean remove;

    @Getter
    private final Map<String, FileProperties> fileProperties = new HashMap<>();

    @Override
    public void apply(DownloadEvent event) {
        events.add(event);
        synchronized (this) {
            if (event instanceof DownloadInitialized) {
                apply((DownloadInitialized) event);
            } else if (event instanceof FileDownloadRequested) {
                apply((FileDownloadRequested) event);
            } else if (event instanceof AllFilesFetched) {
                apply((AllFilesFetched) event);
            } else if (event instanceof DownloadConfirmed) {
                apply((DownloadConfirmed) event);
            } else if (event instanceof PostProcessingLaunched) {
                apply((PostProcessingLaunched) event);
            } else if (event instanceof PostProcessingFinished) {
                apply((PostProcessingFinished) event);
            } else if (event instanceof PostProcessingFailed) {
                apply((PostProcessingFailed) event);
            } else if (event instanceof DownloadCancelled) {
                apply((DownloadCancelled) event);
            } else if (event instanceof DownloadFailed) {
                apply((DownloadFailed) event);
            } else if (event instanceof FilesListFetched) {
                this.apply((FilesListFetched) event);
            } else if (event instanceof FileDownloaded) {
                this.apply((FileDownloaded) event);
            } else if (event instanceof FileRemoved) {
                apply((FileRemoved) event);
            }
        }
    }

    private void apply(DownloadInitialized event) {
        this.state = REQUESTED;
        this.connection = event.getConnection();
        this.email = event.getEmail();
        this.description = event.getDescription();
        this.folder = event.getFolder();
        this.files = event.getFiles();
        this.remove = event.getRemove();
    }

    private void apply(FileDownloadRequested event) {
        state = IN_PROGRESS;
        final String name = new File(event.getFilename()).getName();
        FileProperties props = new FileProperties(name, DownloadFileState.IN_PROGRESS, event.getFilename());
        this.fileProperties.put(name, props);
    }

    private void apply(FilesListFetched event) {
        this.files = event.getFiles();
    }

    private void apply(FileDownloaded event) {
        FileProperties props = this.fileProperties.get(event.getFilename());
        props.setState(PREPARED);
        props.setSize(event.getSize());
        props.setHash(event.getHash());
        props.setTempId(event.getTempId());
    }

    private void apply(FileRemoved event) {
        this.fileProperties.get(event.getFilename()).setState(REMOVED_FROM_TEMPORARY_STORE);
    }

    private void apply(AllFilesFetched event) {
        state = READY;
    }

    private void apply(DownloadConfirmed event) {
        state = DOWNLOADED;
    }

    private void apply(PostProcessingLaunched event) {
        state = POST_PROCESSING;
    }

    private void apply(PostProcessingFinished event) {
        state = FINISHED;
    }

    private void apply(PostProcessingFailed event) {
        state = FINISHED_WITH_WARNING;
    }

    private void apply(DownloadCancelled event) {
        state = CANCELED;
    }

    private void apply(DownloadFailed event) {
        state = FAILED;
    }

    public boolean canContinue() {
        return of(CANCELED, FAILED).noneMatch(it -> it.equals(this.getState()));
    }

    public boolean isNew() {
        return this.events.isEmpty();
    }

    @Override
    public void accept(Visitor<DownloadEvent> visitor) {
        events.forEach(visitor::visit);
    }

    public void startDownload(String connection, String email, String folder, String description, Set<String> files, boolean remove) {
        publisher.publish(new DownloadInitialized(getId(), connection, description, email, folder, files, remove));
    }

    public void confirm() {
        if (!READY.equals(state)) {
            throw invalidConfirmAction(state);
        }
        publisher.publish(new DownloadConfirmed(getId()));
        new Thread(() -> publisher.publish(new PostProcessingLaunched(getId()))).start();
    }

    public void cancel(String reason) {
        if (of(REQUESTED, IN_PROGRESS, READY).noneMatch(it -> it.equals(state))) {
            throw invalidCancelAction(state);
        }
        new Thread(() -> publisher.publish(new DownloadCancelled(getId(), reason))).start();
    }

}
