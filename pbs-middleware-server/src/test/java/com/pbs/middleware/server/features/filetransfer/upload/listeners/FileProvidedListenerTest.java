package com.pbs.middleware.server.features.filetransfer.upload.listeners;

import com.pbs.middleware.server.features.filetransfer.upload.events.FileProvided;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploadStarted;
import com.pbs.middleware.server.features.filetransfer.upload.status.repository.UploadFileStatus;
import com.pbs.middleware.server.features.filetransfer.upload.status.repository.UploadStatus;
import com.pbs.middleware.server.features.filetransfer.upload.status.service.UploadStatusService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("FileProvidedListener tests")
class FileProvidedListenerTest {

    @Mock
    private UploadPublisher publisher;

    @Mock
    private UploadStatusService uploadStatusService;

    @InjectMocks
    private FileProvidedListener listener;

    @Test
    @DisplayName("onApplicationEvent test")
    void onApplicationEvent_test() {
        final UUID domainId = UUID.randomUUID();
        final String filename = "filename";
        FileProvided event = new FileProvided(domainId, filename, 1L, "-", filename + "_id");

        doNothing().when(publisher).publish(any(FileUploadStarted.class));
        doNothing().when(uploadStatusService).create(any(UploadFileStatus.class));

        listener.onApplicationEvent(event);

        ArgumentCaptor<FileUploadStarted> eventCaptor = ArgumentCaptor.forClass(FileUploadStarted.class);
        ArgumentCaptor<UploadFileStatus> entityCaptor = ArgumentCaptor.forClass(UploadFileStatus.class);

        verify(publisher).publish(eventCaptor.capture());
        verify(publisher, times(1)).publish(any(FileUploadStarted.class));
        verify(publisher, never()).publishAsync(any());

        FileUploadStarted published = eventCaptor.getValue();
        assertNotNull(published);
        assertEquals(domainId, published.getDomainId());
        assertEquals(filename, published.getFilename());

        verify(uploadStatusService).create(entityCaptor.capture());
        verify(uploadStatusService, times(1)).create(any(UploadFileStatus.class));

        UploadFileStatus created = entityCaptor.getValue();
        assertNotNull(created);
        assertEquals(domainId, created.getId());
        assertEquals(filename, created.getFilename());
        assertNull(created.getError());
        assertEquals(filename, published.getFilename());

        UploadStatus status = created.getUploadStatus();
        assertNotNull(status);
        assertEquals(domainId, status.getId());
        assertFalse(status.getNotified());
        assertNotNull(status.getStartDate());
        assertEquals(1, status.getFiles().size());
        assertEquals(created, status.getFiles().iterator().next());
    }

}