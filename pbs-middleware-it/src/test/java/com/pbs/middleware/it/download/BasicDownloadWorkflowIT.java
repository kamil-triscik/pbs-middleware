package com.pbs.middleware.it.download;

import com.pbs.middleware.api.connection.Connection;
import com.pbs.middleware.api.download.Download;
import com.pbs.middleware.api.download.DownloadRequest;
import com.pbs.middleware.it.config.MiddlewareIntegrationTest;
import com.pbs.middleware.it.config.utils.DownloadUtils;
import com.pbs.middleware.server.MiddlewareServer;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import com.pbs.middleware.server.features.filetransfer.download.events.AllFilesFetched;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadConfirmed;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadInitialized;
import com.pbs.middleware.server.features.filetransfer.download.events.EmailSentFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloadRequested;
import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloaded;
import com.pbs.middleware.server.features.filetransfer.download.events.FileRemoved;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingFinished;
import com.pbs.middleware.server.features.filetransfer.download.events.PostProcessingLaunched;
import com.pbs.middleware.server.features.ssh.scp.SCPFactory;
import cz.muni.ll.middleware.client.download.client.DownloadClient;
import cz.muni.ll.middleware.client.download.domain.DownloadRequestBuilder;
import cz.muni.ll.middleware.client.download.domain.FileContent;
import cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable.ForbiddenException;
import java.nio.file.Paths;
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

import static com.pbs.middleware.api.download.DownloadState.FAILED;
import static com.pbs.middleware.api.download.DownloadState.FINISHED;
import static com.pbs.middleware.api.download.DownloadState.READY;
import static com.pbs.middleware.it.config.utils.ConnectionUtils.createConnection;
import static com.pbs.middleware.it.config.utils.DownloadUtils.getClient;
import static com.pbs.middleware.it.config.utils.DownloadUtils.waitTill;
import static java.time.Duration.ofMinutes;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MiddlewareServer.class
)
@Testcontainers
public class BasicDownloadWorkflowIT extends MiddlewareIntegrationTest {

    @Autowired
    public GenericContainer<?> ubuntuContainer;

    @Autowired
    public TemporaryStorage temporaryStorage;

    @Autowired
    public SCPFactory scpFactory;

    @Test
    void downloadTest() throws Exception {
        final String content = "test_string";
        final String folder = "/home/user/";
        final String filename = "down.txt";

        Connection connection = createConnection(remoteUrl, email, password, mockMvc);
        prepareFileForDownload(connection, folder, filename, content);

        DownloadClient client = getClient(serverPort, user1.getEmail(), user1.getPassword());

        DownloadRequest request = new DownloadRequestBuilder(connection.getName())
                .file(filename)
                .folder(folder)
                .remove(true)
                .build();

        UUID downloadId = client.startDownload(request);

        AtomicReference<Download> downloadRef = new AtomicReference<>();

        Predicate<Download> tillDownloaded = it -> !READY.equals(it.getState()) && !FAILED.equals(it.getState());
        assertTimeout(ofMinutes(2), () -> downloadRef.set(waitTill(downloadId, client, tillDownloaded)), "Download timeout!");

        assertEquals("Wrong state", READY, downloadRef.get().getState());

        FileContent file = client.downloadFileContent(downloadId, filename);

        assertEquals("Wrong filename", filename, file.getFilename());
        assertEquals("Wrong size", content.length(), file.getSize());
        assertArrayEquals("Wrong content", content.getBytes(), file.getData());

        client.confirm(downloadId);

        Thread.sleep(2000);

        // use API one
        Download download = client.getDownload(downloadId);

        assertEquals("Wrong filename", FINISHED, download.getState());

        assertThrows(
                ForbiddenException.class,
                () -> DownloadUtils.getClient(serverPort, user2.getEmail(), user2.getPassword()).getDownload(downloadId));


        DownloadUtils.getEvents(mockMvc, downloadId, user1, status().isUnauthorized());

        Map<String, String> events = DownloadUtils.getEvents(mockMvc, downloadId, getAdmin());
        List<String> eventsList = new ArrayList<>(events.keySet());
        assertEquals("Wrong number of events", 8, events.size());

        assertTrue(events.containsKey(DownloadInitialized.class.getSimpleName()));
        assertNotNull(events.get(DownloadInitialized.class.getSimpleName()));
        assertEquals(eventsList.get(0), DownloadInitialized.class.getSimpleName());
        assertTrue(events.containsKey(FileDownloadRequested.class.getSimpleName()));
        assertNotNull(events.get(FileDownloadRequested.class.getSimpleName()));
        assertEquals(eventsList.get(1), FileDownloadRequested.class.getSimpleName());
        assertTrue(events.containsKey(FileDownloaded.class.getSimpleName()));
        assertNotNull(events.get(FileDownloaded.class.getSimpleName()));
        assertEquals(eventsList.get(2), FileDownloaded.class.getSimpleName());
        assertTrue(events.containsKey(AllFilesFetched.class.getSimpleName()));
        assertNotNull(events.get(AllFilesFetched.class.getSimpleName()));
        assertEquals(eventsList.get(3), AllFilesFetched.class.getSimpleName());
        assertTrue(events.containsKey(PostProcessingLaunched.class.getSimpleName()));
        assertNotNull(events.get(PostProcessingLaunched.class.getSimpleName()));
        assertEquals(eventsList.get(4), DownloadConfirmed.class.getSimpleName());
        assertTrue(events.containsKey(PostProcessingLaunched.class.getSimpleName()));
        assertNotNull(events.get(PostProcessingLaunched.class.getSimpleName()));
        assertEquals(eventsList.get(5), PostProcessingLaunched.class.getSimpleName());
        assertTrue(events.containsKey(FileRemoved.class.getSimpleName()));
        assertNotNull(events.get(FileRemoved.class.getSimpleName()));
        assertEquals(eventsList.get(6), FileRemoved.class.getSimpleName());
        assertTrue(events.containsKey(PostProcessingFinished.class.getSimpleName()));
        assertNotNull(events.get(PostProcessingFinished.class.getSimpleName()));
        assertEquals(eventsList.get(7), PostProcessingFinished.class.getSimpleName());

        Thread.sleep(5000);

        // check file was removed
        Container.ExecResult res = ubuntuContainer.execInContainer("ls", folder);
        assertFalse(res.getStdout().contains(filename));
        assertEquals("", res.getStderr());
        assertEquals(0, res.getExitCode());

        getClient(serverPort, email, password).getDownload(downloadId);

        //delete all events
        client.delete(downloadId);

        //check that temporary data was removed
        assertTrue(temporaryStorage.findByDomainId(downloadId).isEmpty());

        //check that upload is not available anymore
        DownloadUtils.getEvents(mockMvc, downloadId, getAdmin(), status().isNotFound());

    }

    private void prepareFileForDownload(Connection connection, String folder, String filename, String content) throws Exception, InterruptedException {
        ubuntuContainer.execInContainer("mkdir", "-p", folder);
        scpFactory.get(connection.getId()).upload(content.getBytes(), Paths.get(folder + filename));

        Container.ExecResult res = ubuntuContainer.execInContainer("ls", folder);
        assertTrue(res.getStdout().contains(filename));
        assertEquals("", res.getStderr());
        assertEquals(0, res.getExitCode());
    }

}
