package com.pbs.middleware.it.upload;

import com.pbs.middleware.api.connection.Connection;
import com.pbs.middleware.api.upload.Upload;
import com.pbs.middleware.it.config.MiddlewareIntegrationTest;
import com.pbs.middleware.it.config.utils.UploadUtils;
import com.pbs.middleware.server.MiddlewareServer;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileProvided;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploadStarted;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploaded;
import com.pbs.middleware.server.features.filetransfer.upload.events.TemporaryFileDeleted;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFinished;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadInitialized;
import cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable.ForbiddenException;
import cz.muni.ll.middleware.client.upload.client.UploadClient;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.pbs.middleware.api.upload.UploadState.UPLOADED;
import static com.pbs.middleware.it.config.utils.ConnectionUtils.createConnection;
import static com.pbs.middleware.it.config.utils.UploadUtils.getClient;
import static com.pbs.middleware.it.config.utils.UploadUtils.getFile;
import static com.pbs.middleware.it.config.utils.UploadUtils.getUploadRequest;
import static com.pbs.middleware.it.config.utils.UploadUtils.waitTill;
import static java.time.Duration.ofMinutes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MiddlewareServer.class
)
@Testcontainers
public class BasicUploadWorkflowIT extends MiddlewareIntegrationTest {

    @Autowired
    public GenericContainer<?> ubuntuContainer;

    @Autowired
    public TemporaryStorage temporaryStorage;

    @Test
    void uploadTest() throws Exception {
        UploadClient client = getClient(serverPort, user1.getEmail(), user1.getPassword());

        final String content = "test_string";
        final String destination = "/home/user/";
        final File file = getFile(content);

        Connection conn = createConnection(remoteUrl, email, password, mockMvc);
        UUID uploadId = client.uploadData(getUploadRequest(conn, file, destination));
        AtomicReference<Upload> uploadRef = new AtomicReference<>();

        Predicate<Upload> tillUploaded = it -> !UPLOADED.equals(it.getState());

        assertTimeout(ofMinutes(2), () -> uploadRef.set(waitTill(uploadId, client, tillUploaded)), "Upload timeout!");
        Upload upload = uploadRef.get();

        assertNotNull("Upload should not be null!", upload);
        assertEquals(UPLOADED, uploadRef.get().getState());

        // check that file was uploaded
        Container.ExecResult res = ubuntuContainer.execInContainer("ls", destination);
        assertEquals(0, res.getExitCode());
        assertTrue(res.getStdout().contains(file.getName()));

        //check of uploaded file content
        res = ubuntuContainer.execInContainer("cat", destination + file.getName());
        assertEquals(0, res.getExitCode());
        assertEquals(res.getStdout(), content);

        //check that non admin is not allowed to access events
        UploadUtils.getEvents(mockMvc, uploadId, user1, status().isUnauthorized());

        //check if all events are present and correct order
        Map<String, String> events = UploadUtils.getEvents(mockMvc, uploadId, getAdmin());
        List<String> eventsList = new ArrayList<>(events.keySet());
        assertEquals("Wrong number of events", 6, events.size());
        assertTrue(events.containsKey(UploadInitialized.class.getSimpleName()));
        assertNotNull(events.get(UploadInitialized.class.getSimpleName()));
        assertEquals(eventsList.get(0), UploadInitialized.class.getSimpleName());
        assertTrue(events.containsKey(FileProvided.class.getSimpleName()));
        assertNotNull(events.get(FileProvided.class.getSimpleName()));
        assertEquals(eventsList.get(1), FileProvided.class.getSimpleName());
        assertTrue(events.containsKey(FileUploadStarted.class.getSimpleName()));
        assertNotNull(events.get(FileUploadStarted.class.getSimpleName()));
        assertEquals(eventsList.get(2), FileUploadStarted.class.getSimpleName());
        assertTrue(events.containsKey(FileUploaded.class.getSimpleName()));
        assertNotNull(events.get(FileUploaded.class.getSimpleName()));
        assertEquals(eventsList.get(3), FileUploaded.class.getSimpleName());
        assertTrue(events.containsKey(UploadFinished.class.getSimpleName()));
        assertNotNull(events.get(UploadFinished.class.getSimpleName()));
        assertEquals(eventsList.get(4), UploadFinished.class.getSimpleName());
        assertTrue(events.containsKey(TemporaryFileDeleted.class.getSimpleName()));
        assertNotNull(events.get(TemporaryFileDeleted.class.getSimpleName()));
        assertEquals(eventsList.get(5), TemporaryFileDeleted.class.getSimpleName());

        //check that another user is not allowed to access upload
        assertThrows(
                ForbiddenException.class,
                () -> getClient(serverPort, user2.getEmail(), user2.getPassword()).getUpload(uploadId));

        //check that admin can access upload data
        getClient(serverPort, email, password).getUpload(uploadId);

        //delete all events
        client.delete(uploadId);

        //check that temporary data was removed
        assertTrue(temporaryStorage.findByDomainId(uploadId).isEmpty());

        //check that upload is not available anymore
        UploadUtils.getEvents(mockMvc, uploadId, getAdmin(), status().isNotFound());
    }

}
