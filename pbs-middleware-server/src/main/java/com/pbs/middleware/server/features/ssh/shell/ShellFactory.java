package com.pbs.middleware.server.features.ssh.shell;

import com.pbs.middleware.server.features.connection.repository.Connection;
import com.pbs.middleware.server.features.connection.repository.PasswordConnection;
import com.pbs.middleware.server.features.connection.repository.SshKeyConnection;
import com.pbs.middleware.server.features.connection.service.ConnectionService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShellFactory {

    private final ConnectionService connectionService;

    public Shell get(UUID connection) {
        return get(connectionService.get(connection));
    }

    public Shell get(String connection) {
        return get(connectionService.get(connection));
    }

    public Shell get(Connection connection) {
        return new ShellDecorator(getShell(connection));
    }

    private CloseableShell getShell(Connection connection) {
        if (connection instanceof PasswordConnection) {
            return new PasswordShell((PasswordConnection) connection);
        } else if (connection instanceof SshKeyConnection) {
            return new SshKeyShell((SshKeyConnection) connection);
        } else {
            return new LocalShell(connection);
        }
    }
}
