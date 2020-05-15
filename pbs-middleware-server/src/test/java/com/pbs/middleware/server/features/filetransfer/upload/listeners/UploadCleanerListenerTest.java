package com.pbs.middleware.server.features.filetransfer.upload.listeners;

import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorageException;
import com.pbs.middleware.server.features.filetransfer.upload.domain.FileProperties;
import com.pbs.middleware.server.features.filetransfer.upload.domain.Upload;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploaded;
import com.pbs.middleware.server.features.filetransfer.upload.events.TemporaryFileDeleted;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadCancelled;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadEvent;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFailed;
import com.pbs.middleware.server.features.filetransfer.upload.factory.UploadFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState.REMOVED_FROM_TEMPORARY_STORAGE;
import static com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState.UPLOADED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("UploadCleanerListener tests")
class UploadCleanerListenerTest {

    private static final UUID domainId = UUID.randomUUID();
    private static final String filename1 = "filename1";
    private static final String tempId1 = "tempId1";
    private static final String filename2 = "filename2";
    private static final String tempId2 = "tempId2";

    @Mock
    private UploadPublisher publisher;

    @Mock
    private TemporaryStorage temporaryStorage;

    @Mock
    private UploadFactory uploadFactory;

    @InjectMocks
    private UploadCleanerListener listener;

    @BeforeEach
    void reset() {
        Mockito.reset(publisher);
        Mockito.reset(temporaryStorage);
        Mockito.reset(uploadFactory);
    }

    private static Stream<Arguments> events() {
        return Stream.of(
                Arguments.of(new FileUploaded(domainId, filename1, 1L), FileUploaded.class.getSimpleName()),
                Arguments.of(new UploadCancelled(domainId, "-", false), UploadCancelled.class.getSimpleName()),
                Arguments.of(new UploadFailed(domainId), UploadFailed.class.getSimpleName())
        );
    }

    @ParameterizedTest(name = "Test {index}: Event type: {1}")
    @MethodSource("events")
    @DisplayName("onApplicationEvent: temporary storage cleaning test")
    void onApplicationEvent_temp_storage_cleaning_test(UploadEvent event, String eventType) throws IllegalAccessException, TemporaryStorageException {
        Upload upload = new Upload(domainId, publisher);

        FileProperties file1 = new FileProperties(filename1, UPLOADED);
        file1.setTempId(tempId1);
        FileProperties file2 = new FileProperties(filename2, REMOVED_FROM_TEMPORARY_STORAGE);
        file2.setTempId(tempId2);
        Map<String, FileProperties> files = new HashMap<>();
        files.put(filename1, file1);
        files.put(filename2, file2);

        FieldUtils.writeField(upload, "files", files, true);
        FieldUtils.writeField(upload, "filenames", Set.of(filename1, filename2), true);

        when(uploadFactory.get(domainId)).thenReturn(upload);

        if (event instanceof FileUploaded) {
            listener.onApplicationEvent((FileUploaded) event);
        } else if (event instanceof UploadFailed) {
            listener.onApplicationEvent((UploadFailed) event);
        } else if (event instanceof UploadCancelled) {
            listener.onApplicationEvent((UploadCancelled) event);
        }

        ArgumentCaptor<TemporaryFileDeleted> publishCaptor = ArgumentCaptor.forClass(TemporaryFileDeleted.class);

        verify(uploadFactory, times(1)).get(eq(domainId));
        verify(temporaryStorage, times(1)).deleteById(eq(domainId), eq(tempId1));
        verify(publisher, times(1)).publish(any(TemporaryFileDeleted.class));
        verify(publisher, never()).publishAsync(any(TemporaryFileDeleted.class));
        verify(publisher, times(1)).publish(publishCaptor.capture());

        TemporaryFileDeleted capturedEvent = publishCaptor.getValue();
        assertNotNull(capturedEvent);
        assertEquals(domainId, capturedEvent.getDomainId());
        assertEquals(tempId1, capturedEvent.getTempId());
    }

    @Test
    @DisplayName("onApplicationEvent: temporary storage cleaning exception test")
    void onApplicationEvent_temp_storage_cleaning_exception_test() throws IllegalAccessException, TemporaryStorageException {
        Upload upload = new Upload(domainId, publisher);

        FileProperties file1 = new FileProperties(filename1, UPLOADED);
        file1.setTempId(tempId1);
        FileProperties file2 = new FileProperties(filename2, REMOVED_FROM_TEMPORARY_STORAGE);
        file2.setTempId(tempId2);
        Map<String, FileProperties> files = new HashMap<>();
        files.put(filename1, file1);
        files.put(filename2, file2);

        FieldUtils.writeField(upload, "files", files, true);
        FieldUtils.writeField(upload, "filenames", Set.of(filename1, filename2), true);

        when(uploadFactory.get(eq(domainId))).thenReturn(upload);
        when(temporaryStorage.deleteById(eq(domainId), eq(tempId1))).thenThrow(new TemporaryStorageException("Temp exception"));

        listener.onApplicationEvent(new FileUploaded(domainId, filename1, 1L));

        verify(uploadFactory, times(1)).get(eq(domainId));
        verify(temporaryStorage, times(1)).deleteById(eq(domainId), eq(tempId1));
        verify(publisher, never()).publish(any(TemporaryFileDeleted.class));
        verify(publisher, never()).publishAsync(any(TemporaryFileDeleted.class));

    }

}