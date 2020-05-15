package com.pbs.middleware.server.features.filetransfer.upload.listeners;

import com.pbs.middleware.server.features.filetransfer.upload.domain.Upload;
import com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploaded;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFinished;
import com.pbs.middleware.server.features.filetransfer.upload.factory.UploadFactory;
import java.util.UUID;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("FileUploadedListener tests")
class FileUploadedListenerTest {

    @Mock
    private UploadFactory uploadFactory;

    @Mock
    private UploadPublisher publisher;

    @InjectMocks
    private FileUploadedListener listener;

    @BeforeEach
    void clean() {
        reset(uploadFactory);
        reset(publisher);
    }

    @Test
    @DisplayName("Not all files uploaded")
    void notAllFilesUploaded_test() throws IllegalAccessException {
        final UUID domainId = UUID.randomUUID();
        Upload upload = new Upload(domainId, publisher);
        FieldUtils.writeField(upload, "state", UploadState.IN_PROGRESS, true);

        when(uploadFactory.get(any())).thenReturn(upload);

        listener.onEvent(new FileUploaded(domainId, "", 1L));

        verify(publisher, never()).publish(any(UploadFinished.class));
        verify(publisher, never()).publishAsync(any(UploadFinished.class));
    }

    @Test
    @DisplayName("All files uploaded")
    void allFilesUploaded_test() throws IllegalAccessException {
        final UUID domainId = UUID.randomUUID();
        Upload upload = new Upload(domainId, publisher);
        FieldUtils.writeField(upload, "state", UploadState.UPLOADED, true);

        when(uploadFactory.get(any())).thenReturn(upload);

        listener.onEvent(new FileUploaded(domainId, "", 1L));

        verify(publisher, times(1)).publish(any(UploadFinished.class));
        verify(publisher, never()).publishAsync(any(UploadFinished.class));

        ArgumentCaptor<UploadFinished> captor = ArgumentCaptor.forClass(UploadFinished.class);
        verify(publisher).publish(captor.capture());

        UploadFinished uploadFinished = captor.getValue();
        assertNotNull(uploadFinished);
        assertEquals(domainId, uploadFinished.getDomainId());
    }

}