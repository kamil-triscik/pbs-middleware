package com.pbs.middleware.server.features.filetransfer.upload.service;

import com.pbs.middleware.api.upload.File;
import com.pbs.middleware.api.upload.Upload;
import com.pbs.middleware.api.upload.UploadState;
import com.pbs.middleware.server.features.filetransfer.upload.domain.FileProperties;
import com.pbs.middleware.server.features.filetransfer.upload.domain.UploadFileState;
import com.pbs.middleware.server.features.filetransfer.upload.listeners.UploadPublisher;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Upload Mapper tests")
class UploadMapperTest {

    private final UploadPublisher publisher = new UploadPublisher(null, null);

    @Test
    @DisplayName("Basic fields mapping test")
    void basicFieldsMapper() throws IllegalAccessException {
        final UUID id = UUID.randomUUID();
        final String connection = "connection";
        final String destination = "destination";
        final String description = "description";

        com.pbs.middleware.server.features.filetransfer.upload.domain.Upload upl = new com.pbs.middleware.server.features.filetransfer.upload.domain.Upload(id, publisher);

        FieldUtils.writeField(upl, "connection", connection, true);
        FieldUtils.writeField(upl, "destination", destination, true);
        FieldUtils.writeField(upl, "description", description, true);

        Upload upload = new UploadMapper().toDto(upl);

        assertEquals(id, upload.getId(), "ID incorrectly mapped");
        assertEquals(connection, upload.getConnection(), "Connection incorrectly mapped");
        assertEquals(destination, upload.getDestination(), "Destination incorrectly mapped");
        assertEquals(description, upload.getDescription(), "Description incorrectly mapped");

    }

    @Test
    @DisplayName("Files mapping test")
    void uploadFilesList() throws IllegalAccessException {
        final UUID id = UUID.randomUUID();

        final String filename = "file1";
        final UploadFileState state = UploadFileState.UPLOADED;
        final String hash = "--";
        final Long size = 10L;

        com.pbs.middleware.server.features.filetransfer.upload.domain.Upload upl = new com.pbs.middleware.server.features.filetransfer.upload.domain.Upload(id, publisher);


        FileProperties fileProps = new FileProperties(filename, state);
        fileProps.setHash(hash);
        fileProps.setSize(size);

        FieldUtils.writeField(upl, "filenames", Set.of(filename), true);
        FieldUtils.writeField(upl, "files", Map.of(filename, fileProps), true);

        Upload upload = new UploadMapper().toDto(upl);

        List<File> files = upload.getFiles();
        assertFalse("Files list should not be empty", files.isEmpty());
        assertEquals(1, files.size(), "Files list should contain one file!");

        File file = files.get(0);
        assertEquals(filename, file.getName(), "Wrong filename!");
        assertEquals(state.toString(), file.getState().toString(), "Wrong file state!");
        assertEquals(hash, file.getHash(), "Wrong file hash!");
        assertEquals(size, file.getSize(), "Wrong file size!");

    }

    private static Map<com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState, UploadState> expectedStates = Map.of(
            com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState.INITIALIZED, UploadState.QUEUED,
            com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState.IN_PROGRESS, UploadState.IN_PROGRESS,
            com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState.UPLOADED, UploadState.UPLOADED,
            com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState.FAILED, UploadState.FAILED,
            com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState.CANCELLED, UploadState.CANCELLED
    );

    private static Stream<Arguments> uploadFileStates() {
        return Stream.of(com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState.values())
                .map(it -> Arguments.of(it, expectedStates.get(it)));
    }

    @ParameterizedTest(name = "Test {index}: {0} -> {1}")
    @MethodSource("uploadFileStates")
    @DisplayName("Upload state test")
    void uploadStateTest(com.pbs.middleware.server.features.filetransfer.upload.domain.UploadState state, UploadState expectedResult) throws IllegalAccessException {
        com.pbs.middleware.server.features.filetransfer.upload.domain.Upload upl
                = new com.pbs.middleware.server.features.filetransfer.upload.domain.Upload(UUID.randomUUID(), publisher);
        FieldUtils.writeField(upl, "state", state, true);

        Upload upload = new UploadMapper().toDto(upl);

        assertEquals(expectedResult, upload.getState());
    }

}