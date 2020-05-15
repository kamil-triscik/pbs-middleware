package com.pbs.middleware.server.features.ssh.scp;

import com.pbs.middleware.server.features.connection.repository.Connection;
import com.pbs.middleware.server.features.connection.repository.PasswordConnection;
import com.pbs.middleware.server.features.connection.repository.SshKeyConnection;
import com.pbs.middleware.server.features.connection.service.ConnectionService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SCPFactory {

    private final ConnectionService connectionService;

    public SCP get(String connection) {
        return get(connectionService.get(connection));
    }

    public SCP get(UUID connection) {
        return get(connectionService.get(connection));
    }

    public SCP get(Connection connection) {
        return new SCPDecorator(getSCP(connection));
    }

    private CloseableSCP getSCP(Connection connection) {
        if (connection instanceof PasswordConnection) {
            return new PasswordSCP((PasswordConnection) connection);
        } else if (connection instanceof SshKeyConnection) {
            return new SshKeySCP((SshKeyConnection) connection);
        } else {
            return null; //todo
        }
    }

}
