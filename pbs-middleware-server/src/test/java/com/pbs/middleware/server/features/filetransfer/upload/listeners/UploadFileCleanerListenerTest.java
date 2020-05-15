package com.pbs.middleware.server.features.filetransfer.upload.listeners;

import com.pbs.middleware.server.features.filetransfer.upload.domain.Upload;
import com.pbs.middleware.server.features.filetransfer.upload.events.RemoteFileDeleted;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadCancelled;
import com.pbs.middleware.server.features.filetransfer.upload.factory.UploadFactory;
import com.pbs.middleware.server.features.ssh.shell.Result;
import com.pbs.middleware.server.features.ssh.shell.Shell;
import com.pbs.middleware.server.features.ssh.shell.ShellException;
import com.pbs.middleware.server.features.ssh.shell.ShellFactory;
import com.pbs.middleware.server.features.ssh.shell.ShellUtils;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("UploadFileCleanerListener tests")
class UploadFileCleanerListenerTest {

    @Mock
    private UploadPublisher publisher;

    @Mock
    private ShellFactory shellFactory;

    @Mock
    private Shell shell;

    @Mock
    private UploadFactory uploadFactory;

    @InjectMocks
    private UploadFileCleanerListener listener;

    @BeforeEach
    void reset() {
        Mockito.reset(publisher);
        Mockito.reset(shellFactory);
        Mockito.reset(shell);
        Mockito.reset(uploadFactory);
    }

    @Test
    @DisplayName("onApplicationEvent: cleaning enabled test")
    void onApplicationEvent_cleaning_enabled_test() throws IllegalAccessException, ShellException {
        final UUID domainId = UUID.randomUUID();
        Upload upload = new Upload(domainId, publisher);

        final String destination = "destination";
        final String connection = "connection";
        final String filename1 = "filename1";
        final String filename2 = "filename2";

        FieldUtils.writeField(upload, "destination", destination, true);
        FieldUtils.writeField(upload, "connection", connection, true);
        FieldUtils.writeField(upload, "filenames", Set.of(filename1, filename2), true);

        when(uploadFactory.get(domainId)).thenReturn(upload);
        when(shellFactory.get(eq(connection))).thenReturn(shell);
        when(shell.executeCommand(any(String.class))).thenReturn(new Result(1, "", ""));

        listener.onApplicationEvent(new UploadCancelled(domainId, "no reason", true));

        ArgumentCaptor<RemoteFileDeleted> publishCaptor = ArgumentCaptor.forClass(RemoteFileDeleted.class);

        verify(uploadFactory, times(1)).get(eq(domainId));
        verify(shellFactory, times(2)).get(eq(connection));
        verify(publisher, times(2)).publish(any(RemoteFileDeleted.class));
        verify(publisher, never()).publishAsync(any());
        verify(publisher, times(2)).publish(publishCaptor.capture());

        verify(shell, times(2)).executeCommand(any(String.class));
        verify(shell, times(1)).executeCommand(ShellUtils.removeFileCmd(Path.of(destination, filename1)));
        verify(shell, times(1)).executeCommand(ShellUtils.removeFileCmd(Path.of(destination, filename2)));

        List<RemoteFileDeleted> events = publishCaptor.getAllValues();
        assertEquals(2, events.size());
        assertEquals(1, events.stream()
                .filter(it -> domainId.equals(it.getDomainId()))
                .filter(it -> filename1.equals(it.getFilename()))
                .filter(it -> Path.of(destination, filename1).toString().equals(it.getRemotePath()))
                .count());
        assertEquals(1, events.stream()
                .filter(it -> domainId.equals(it.getDomainId()))
                .filter(it -> filename2.equals(it.getFilename()))
                .filter(it -> Path.of(destination, filename2).toString().equals(it.getRemotePath()))
                .count());
    }

    @Test
    @DisplayName("onApplicationEvent: cleaning disabled test")
    void onApplicationEvent_cleaning_disabled_test() {
        final UUID domainId = UUID.randomUUID();

        listener.onApplicationEvent(new UploadCancelled(domainId, "no reason", false));

        final String connection = "connection";

        verify(uploadFactory, never()).get(eq(domainId));
        verify(shellFactory, never()).get(eq(connection));
        verify(publisher, never()).publish(any(RemoteFileDeleted.class));
        verify(publisher, never()).publishAsync(any());
    }

    @Test
    @DisplayName("onApplicationEvent: shell exception test")
    void onApplicationEvent_shell_exception_test() throws IllegalAccessException, ShellException {
        final UUID domainId = UUID.randomUUID();
        Upload upload = new Upload(domainId, publisher);

        final String destination = "destination";
        final String connection = "connection";
        final String filename1 = "filename1";
        final String filename2 = "filename2";

        FieldUtils.writeField(upload, "destination", destination, true);
        FieldUtils.writeField(upload, "connection", connection, true);
        FieldUtils.writeField(upload, "filenames", Set.of(filename1, filename2), true);

        final String rm1 = ShellUtils.removeFileCmd(Path.of(destination, filename1));
        final String rm2 = ShellUtils.removeFileCmd(Path.of(destination, filename2));

        when(uploadFactory.get(domainId)).thenReturn(upload);
        when(shellFactory.get(eq(connection))).thenReturn(shell);
        when(shell.executeCommand(eq(rm1))).thenReturn(new Result(1, "", ""));
        when(shell.executeCommand(eq(rm2))).thenThrow(new ShellException("shell exception"));

        listener.onApplicationEvent(new UploadCancelled(domainId, "no reason", true));

        ArgumentCaptor<RemoteFileDeleted> publishCaptor = ArgumentCaptor.forClass(RemoteFileDeleted.class);

        verify(uploadFactory, times(1)).get(eq(domainId));
        verify(shellFactory, times(2)).get(eq(connection));
        verify(publisher, times(1)).publish(any(RemoteFileDeleted.class));
        verify(publisher, never()).publishAsync(any());
        verify(publisher).publish(publishCaptor.capture());

        verify(shell, times(2)).executeCommand(any(String.class));
        verify(shell, times(1)).executeCommand(ShellUtils.removeFileCmd(Path.of(destination, filename1)));
        verify(shell, times(1)).executeCommand(ShellUtils.removeFileCmd(Path.of(destination, filename2)));

        List<RemoteFileDeleted> events = publishCaptor.getAllValues();
        assertEquals(1, events.size());
        assertEquals(1, events.stream()
                .filter(it -> domainId.equals(it.getDomainId()))
                .filter(it -> filename1.equals(it.getFilename()))
                .filter(it -> Path.of(destination, filename1).toString().equals(it.getRemotePath()))
                .count());
        assertEquals(0, events.stream()
                .filter(it -> domainId.equals(it.getDomainId()))
                .filter(it -> filename2.equals(it.getFilename()))
                .filter(it -> Path.of(destination, filename2).toString().equals(it.getRemotePath()))
                .count());
    }

}