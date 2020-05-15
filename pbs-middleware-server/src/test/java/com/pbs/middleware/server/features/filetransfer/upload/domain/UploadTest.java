package com.pbs.middleware.server.features.filetransfer.upload.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Upload class tests")
class UploadTest {

//    @Mock
//    private UploadPublisher publisher;
//
//    @BeforeEach
//    void clean() {
//        reset(publisher);
//    }
//
//    @Test
//    @DisplayName("Start upload method test")
//    void startTest() {
//        doNothing().when(publisher).publishAsync(any());
//
//        final UUID domainId = UUID.randomUUID();
//        Upload upload = new Upload(domainId, publisher);
//
//        final String destination = "destination";
//        final String connection = "connection";
//        final String email = "email";
//        final String description = "description";
//        final List<FileOptions> files = new ArrayList<>();
//
//        upload.start(destination, connection, email, description, files);
//
//        ArgumentCaptor<UploadInitialized> captor = ArgumentCaptor.forClass(UploadInitialized.class);
//
//        verify(publisher, times(1)).publishAsync(any(UploadInitialized.class));
//        verify(publisher, never()).publish(any());
//
//        Mockito.verify(publisher).publishAsync(captor.capture());
//        UploadInitialized event = captor.getValue();
//        assertNotNull("Missing UploadInitialized event in captor", event);
//
//        assertEquals(domainId, event.getDomainId());
//        assertEquals(destination, event.getDestination(), "Wrong destination");
//        assertEquals(connection, event.getConnection(), "Wrong connection");
//        assertEquals(email, event.getEmail(), "Wrong email");
//        assertEquals(description, event.getDescription(), "Wrong description");
//        assertEquals(files, event.getFiles());
//    }
//
//    private static Stream<Arguments> validCancelStates() {
//        return Stream.of(
//                Arguments.of(UploadState.INITIALIZED),
//                Arguments.of(UploadState.IN_PROGRESS)
//        );
//    }
//
//    @ParameterizedTest(name = "Test {index}: {0}")
//    @MethodSource("validCancelStates")
//    @DisplayName("Cancel upload method test")
//    void cancelTest_valid(UploadState state) throws IllegalAccessException {
//        doNothing().when(publisher).publishAsync(any());
//
//        final UUID domainId = UUID.randomUUID();
//        Upload upload = new Upload(domainId, publisher);
//        FieldUtils.writeField(upload, "state", state, true);
//
//        final String reason = "reason";
//        final boolean removeUploaded = false;
//
//        upload.cancel(reason, removeUploaded);
//
//        ArgumentCaptor<UploadCancelled> captor = ArgumentCaptor.forClass(UploadCancelled.class);
//
//        verify(publisher, times(1)).publishAsync(any(UploadCancelled.class));
//        verify(publisher, never()).publish(any());
//
//        Mockito.verify(publisher).publishAsync(captor.capture());
//        UploadCancelled event = captor.getValue();
//        assertNotNull("Missing UploadInitialized event in captor", event);
//
//        assertEquals(domainId, event.getDomainId());
//        assertEquals(reason, event.getReason(), "Wrong reason");
//        assertEquals(removeUploaded, event.getRemoveUploadedFiles(), "Wrong removeUploaded flag");
//    }
//
//    private static Stream<Arguments> forbiddenCancelStates() {
//        return Stream.of(
//                Arguments.of(UploadState.UPLOADED),
//                Arguments.of(UploadState.CANCELLED),
//                Arguments.of(UploadState.FAILED)
//        );
//    }
//
//    @ParameterizedTest(name = "Test {index}: {0}")
//    @MethodSource("forbiddenCancelStates")
//    @DisplayName("Cancel invalid call of upload method test")
//    void cancelTest_invalid(UploadState state) throws IllegalAccessException {
//        doNothing().when(publisher).publishAsync(any());
//        doNothing().when(publisher).publish(any());
//
//        final UUID domainId = UUID.randomUUID();
//        Upload upload = new Upload(domainId, publisher);
//
//        final String reason = "reason";
//        final boolean removeUploaded = false;
//
//        FieldUtils.writeField(upload, "state", state, true);
//
//        InvalidUploadActionException exception = assertThrows(InvalidUploadActionException.class,
//                () -> upload.cancel(reason, removeUploaded));
//        assertEquals(UPLOAD__FORBIDDEN_CANCEL_ACTION.toString(), exception.getErrorCode(), "Wrong error code");
//
//        verify(publisher, never()).publishAsync(any());
//        verify(publisher, never()).publish(any());
//    }
//
//    @Test
//    @DisplayName("UploadInitialized event test")
//    void uploadInitialized_eventTest() {
//        doNothing().when(publisher).publishAsync(any());
//        doNothing().when(publisher).publish(any());
//
//        final UUID domainId = UUID.randomUUID();
//        Upload upload = new Upload(domainId, publisher);
//
//        final String destination = "destination";
//        final String connection = "connection";
//        final String email = "email";
//        final String description = "description";
//
//        final String filename = "filename";
//        final String rename = "rename";
//        final Long size = -1L;
//        final String hash = "-";
//        final boolean extract = true;
//        final String secured = "true";
//
//        final List<FileOptions> files = new ArrayList<>();
//        FileOptions file = new FileOptions(filename, rename, extract, secured);
//        files.add(file);
//
//        UploadEvent event = new UploadInitialized(domainId, destination, connection, email, description, files);
//
//        upload.apply(event);
//
//        assertEquals(upload.getConnection(), connection, "Wrong connection");
//        assertEquals(upload.getDestination(), destination, "Wrong destination");
//        assertEquals(upload.getDescription(), description, "Wrong description");
//        assertEquals(upload.getEmail(), email, "Wrong email");
//
//        assertEquals(1, upload.getFilenames().size());
//        assertEquals(filename, upload.getFilenames().iterator().next());
//
//        assertEquals(1, upload.getStates().size());
//        assertEquals(UploadFileState.NEW, upload.getStates().get(filename));
//
//        assertEquals(1, upload.getSizes().size());
//        assertEquals(size, upload.getSizes().get(filename));
//
//        assertEquals(1, upload.getHashes().size());
//        assertEquals(hash, upload.getHashes().get(filename));
//
//        assertEquals(UploadState.INITIALIZED, upload.getState());
//
//        assertEquals(1, upload.getEvents().size());
//        assertEquals(event, upload.getEvents().peek());
//
//        verify(publisher, never()).publishAsync(any());
//        verify(publisher, never()).publish(any());
//    }
//
//    @Test
//    @DisplayName("FileUploadStarted event test")
//    void fileUploadStarted_eventTest() throws IllegalAccessException {
//        doNothing().when(publisher).publishAsync(any());
//        doNothing().when(publisher).publish(any());
//
//        final UUID domainId = UUID.randomUUID();
//        Upload upload = new Upload(domainId, publisher);
//        FieldUtils.writeField(upload, "states", new HashMap<>(), true);
//
//        final String filename = "filename";
//
//        UploadEvent event = new FileUploadStarted(domainId, filename);
//
//        upload.apply(event);
//
//        assertEquals(1, upload.getStates().size());
//        assertEquals(UploadFileState.QUEUED, upload.getStates().get(filename));
//
//        assertEquals(UploadState.IN_PROGRESS, upload.getState());
//
//        assertEquals(1, upload.getEvents().size());
//        assertEquals(event, upload.getEvents().peek());
//
//        verify(publisher, never()).publishAsync(any());
//        verify(publisher, never()).publish(any());
//    }
//
//    @Test
//    @DisplayName("TemporaryFileSaved event test")
//    void temporaryFileSaved_eventTest() throws IllegalAccessException {
//        doNothing().when(publisher).publishAsync(any());
//        doNothing().when(publisher).publish(any());
//
//        final UUID domainId = UUID.randomUUID();
//        Upload upload = new Upload(domainId, publisher);
//        final UploadState expectedState = upload.getState();
//        FieldUtils.writeField(upload, "states", new HashMap<>(), true);
//        FieldUtils.writeField(upload, "temporaryFiles", HashBiMap.create(), true);
//
//        final String filename = "filename";
//        final String tempId = "filename_id";
//
//        UploadEvent event = new TemporaryFileSaved(domainId, filename, tempId);
//
//        upload.apply(event);
//
//        assertEquals(1, upload.getStates().size());
//        assertEquals(UploadFileState.TEMPORARY_STORED, upload.getStates().get(filename));
//
//        assertEquals(1, upload.getTemporaryFiles().size());
//        assertEquals(tempId, upload.getTemporaryFiles().get(filename));
//
//        //state should not change
//        assertEquals(expectedState, upload.getState());
//
//        assertEquals(1, upload.getEvents().size());
//        assertEquals(event, upload.getEvents().peek());
//
//        verify(publisher, never()).publishAsync(any());
//        verify(publisher, never()).publish(any());
//    }
//
//    @Test
//    @DisplayName("FileUploaded(1/2 is UPLOADED) event test")
//    void fileUploaded1_eventTest() throws IllegalAccessException {
//        doNothing().when(publisher).publishAsync(any());
//        doNothing().when(publisher).publish(any());
//
//        final UUID domainId = UUID.randomUUID();
//        Upload upload = new Upload(domainId, publisher);
//        FieldUtils.writeField(upload, "states", new HashMap<>(), true);
//
//        final String filename = "filename";
//        upload.getStates().put("anotherFile", UploadFileState.TEMPORARY_STORED);
//        UploadEvent event = new FileUploaded(domainId, filename);
//
//        upload.apply(event);
//
//        assertEquals(2, upload.getStates().size());
//        assertEquals(UploadFileState.UPLOADED, upload.getStates().get(filename));
//
//        //state should not change
//        assertEquals(UploadState.IN_PROGRESS, upload.getState());
//
//        assertEquals(1, upload.getEvents().size());
//        assertEquals(event, upload.getEvents().peek());
//
//        verify(publisher, never()).publishAsync(any());
//        verify(publisher, never()).publish(any());
//    }
//
//    @Test
//    @DisplayName("FileUploaded(all are UPLOADED) event test")
//    void fileUploaded2_eventTest() throws IllegalAccessException {
//        doNothing().when(publisher).publishAsync(any());
//        doNothing().when(publisher).publish(any());
//
//        final UUID domainId = UUID.randomUUID();
//        Upload upload = new Upload(domainId, publisher);
//        final UploadState oldState = upload.getState();
//        FieldUtils.writeField(upload, "states", new HashMap<>(), true);
//
//        final String filename = "filename";
//        upload.getStates().put("anotherFile", UploadFileState.UPLOADED);
//        UploadEvent event = new FileUploaded(domainId, filename);
//
//        upload.apply(event);
//
//        assertEquals(2, upload.getStates().size());
//        assertEquals(UploadFileState.UPLOADED, upload.getStates().get(filename));
//
//        //state should not change
//        assertEquals(UploadState.UPLOADED, upload.getState());
//        assertNotEquals(oldState, upload.getState());
//
//        assertEquals(1, upload.getEvents().size());
//        assertEquals(event, upload.getEvents().peek());
//
//        verify(publisher, never()).publishAsync(any());
//        verify(publisher, never()).publish(any());
//    }
//
//    @Test
//    @DisplayName("UploadFailed event test")
//    void uploadFailed_eventTest() throws IllegalAccessException {
//        doNothing().when(publisher).publishAsync(any());
//        doNothing().when(publisher).publish(any());
//
//        final UUID domainId = UUID.randomUUID();
//        Upload upload = new Upload(domainId, publisher);
//        FieldUtils.writeField(upload, "state", UploadState.IN_PROGRESS, true);
//
//
//        UploadEvent event = new UploadFailed(domainId);
//
//        upload.apply(event);
//
//        assertEquals(UploadState.FAILED, upload.getState());
//
//        assertEquals(1, upload.getEvents().size());
//        assertEquals(event, upload.getEvents().peek());
//
//        verify(publisher, never()).publishAsync(any());
//        verify(publisher, never()).publish(any());
//    }
//
//    @Test
//    @DisplayName("TemporaryFileDeleted event test")
//    void temporaryFileDeleted_eventTest() throws IllegalAccessException {
//        doNothing().when(publisher).publishAsync(any());
//        doNothing().when(publisher).publish(any());
//
//        final UUID domainId = UUID.randomUUID();
//        Upload upload = new Upload(domainId, publisher);
//        final UploadState expectedState = upload.getState();
//        FieldUtils.writeField(upload, "states", new HashMap<>(), true);
//        FieldUtils.writeField(upload, "temporaryFiles", HashBiMap.create(), true);
//
//
//        final String filename = "filename";
//        final String tempId = "filename_id";
//
//        upload.getTemporaryFiles().put(filename, tempId);
//
//        UploadEvent event = new TemporaryFileDeleted(domainId, tempId);
//
//        upload.apply(event);
//
//        assertEquals(1, upload.getStates().size());
//        assertEquals(UploadFileState.REMOVED_FROM_TEMPORARY_STORAGE, upload.getStates().get(filename));
//
//        assertEquals(1, upload.getTemporaryFiles().size());
//        assertEquals(tempId, upload.getTemporaryFiles().get(filename));
//
//        //state should not change
//        assertEquals(expectedState, upload.getState());
//
//        assertEquals(1, upload.getEvents().size());
//        assertEquals(event, upload.getEvents().peek());
//
//        verify(publisher, never()).publishAsync(any());
//        verify(publisher, never()).publish(any());
//    }
//
//    @Test
//    @DisplayName("UploadCancelled(clean) event test")
//    void uploadCancelled_clean_eventTest() throws IllegalAccessException {
//        doNothing().when(publisher).publishAsync(any());
//        doNothing().when(publisher).publish(any());
//
//        final UUID domainId = UUID.randomUUID();
//        Upload upload = new Upload(domainId, publisher);
//        FieldUtils.writeField(upload, "states", new HashMap<>(), true);
//
//
//        final String filename1 = "filename1";
//        final String filename2 = "filename2";
//        FieldUtils.writeField(upload, "filenames", Set.of(filename1, filename2), true);
//
//        upload.getStates().put(filename1, UploadFileState.TEMPORARY_STORED);
//        upload.getStates().put(filename2, UploadFileState.UPLOADED);
//
//        UploadEvent event = new UploadCancelled(domainId, "no reason", true);
//
//        upload.apply(event);
//
//        assertEquals(2, upload.getStates().size());
//        assertTrue(upload.getStates().values().stream().allMatch(it -> it.equals(UploadFileState.CANCELLED)));
//
//        //state should not change
//        assertEquals(UploadState.CANCELLED, upload.getState());
//
//        assertEquals(1, upload.getEvents().size());
//        assertEquals(event, upload.getEvents().peek());
//
//        verify(publisher, never()).publishAsync(any());
//        verify(publisher, never()).publish(any());
//    }
//
//    @Test
//    @DisplayName("UploadCancelled(no_clean) event test")
//    void uploadCancelled_noclean_eventTest() throws IllegalAccessException {
//        doNothing().when(publisher).publishAsync(any());
//        doNothing().when(publisher).publish(any());
//
//        final UUID domainId = UUID.randomUUID();
//        Upload upload = new Upload(domainId, publisher);
//        FieldUtils.writeField(upload, "states", new HashMap<>(), true);
//
//        final String filename1 = "filename1";
//        final String filename2 = "filename2";
//        FieldUtils.writeField(upload, "filenames", Set.of(filename1, filename2), true);
//
//        upload.getStates().put(filename1, UploadFileState.TEMPORARY_STORED);
//        upload.getStates().put(filename2, UploadFileState.UPLOADED);
//
//        UploadEvent event = new UploadCancelled(domainId, "no reason", false);
//
//        upload.apply(event);
//
//        assertEquals(2, upload.getStates().size());
//        assertEquals(UploadFileState.CANCELLED, upload.getStates().get(filename1));
//        assertEquals(UploadFileState.UPLOADED, upload.getStates().get(filename2));
//
//        //state should not change
//        assertEquals(UploadState.CANCELLED, upload.getState());
//
//        assertEquals(1, upload.getEvents().size());
//        assertEquals(event, upload.getEvents().peek());
//
//        verify(publisher, never()).publishAsync(any());
//        verify(publisher, never()).publish(any());
//    }


}