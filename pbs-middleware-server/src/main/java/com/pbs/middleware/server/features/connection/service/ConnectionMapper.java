package com.pbs.middleware.server.features.connection.service;

import com.pbs.middleware.api.connection.CreateConnection;
import com.pbs.middleware.api.connection.UpdateConnection;
import com.pbs.middleware.server.features.connection.repository.Connection;
import com.pbs.middleware.server.features.connection.repository.PasswordConnection;
import com.pbs.middleware.server.features.connection.repository.SshKeyConnection;
import java.util.UUID;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.api.connection.ConnectionType.LOCAL;
import static com.pbs.middleware.api.connection.ConnectionType.PASSWORD_AUTH;
import static com.pbs.middleware.api.connection.ConnectionType.SSH_KEY_AUTH;

/**
 * Refactor to remove duplicates
 */
@Component
public class ConnectionMapper {

    public com.pbs.middleware.api.connection.Connection toDto(Connection connection) {
        com.pbs.middleware.api.connection.Connection connectionDto = new com.pbs.middleware.api.connection.Connection();

        connectionDto.setId(connection.getId());
        connectionDto.setName(connection.getName());
        connectionDto.setDescription(connection.getDescription());
        connectionDto.setPbsHost(connection.getPbsHost());

        if (connection instanceof PasswordConnection) {
            connectionDto.setLogin(((PasswordConnection)connection).getLogin());
            connectionDto.setPassword(((PasswordConnection) connection).getPassword());
            connectionDto.setSshHost(((PasswordConnection) connection).getSshHost());
            connectionDto.setType(PASSWORD_AUTH);
        } else if (connection instanceof SshKeyConnection) {
            connectionDto.setLogin(((SshKeyConnection)connection).getLogin());
            connectionDto.setSshKey(((SshKeyConnection) connection).getSshKey());
            connectionDto.setSshHost(((SshKeyConnection) connection).getSshHost());
            connectionDto.setType(SSH_KEY_AUTH);
        } else {
            connectionDto.setType(LOCAL);
        }

        return connectionDto;
    }

    public Connection fromDto(CreateConnection dto) {
        return fromDto(null, dto);
    }

    public Connection fromDto(UUID id, CreateConnection dto) {
        if (dto.getType() == PASSWORD_AUTH) {
            PasswordConnection connection = new PasswordConnection();
            if (id != null) {
                connection.setId(id);
            }
            connection.setName(dto.getName());
            connection.setDescription(dto.getDescription());
            connection.setPbsHost(dto.getPbsHost());
            connection.setLogin(dto.getLogin());
            connection.setPassword(dto.getPassword());
            connection.setSshHost(dto.getSshHost());
            return connection;
        } else if (dto.getType() == SSH_KEY_AUTH) {
            SshKeyConnection connection = new SshKeyConnection();
            connection.setPbsHost(dto.getPbsHost());
            if (id != null) {
                connection.setId(id);
            }
            connection.setName(dto.getName());
            connection.setDescription(dto.getDescription());
            connection.setLogin(dto.getLogin());
            connection.setSshKey(dto.getSshKey());
            connection.setSshHost(dto.getSshHost());
            return connection;
        } else {
            Connection connection = new Connection();
            if (id != null) {
                connection.setId(id);
            }
            connection.setName(dto.getName());
            connection.setDescription(dto.getDescription());
            connection.setPbsHost(dto.getPbsHost());
            return connection;
        }
    }

    public Connection fromDto(UpdateConnection dto) {
        return fromDto(null, dto);
    }

    public Connection fromDto(UUID id, UpdateConnection dto) {
        if (dto.getType() == PASSWORD_AUTH) {
            PasswordConnection connection = new PasswordConnection();
            if (id != null) {
                connection.setId(id);
            }
            connection.setName(dto.getName());
            connection.setDescription(dto.getDescription());
            connection.setPbsHost(dto.getPbsHost());
            connection.setLogin(dto.getLogin());
            connection.setPassword(dto.getPassword());
            connection.setSshHost(dto.getSshHost());
            return connection;
        } else if (dto.getType() == SSH_KEY_AUTH) {
            SshKeyConnection connection = new SshKeyConnection();
            connection.setPbsHost(dto.getPbsHost());
            if (id != null) {
                connection.setId(id);
            }
            connection.setName(dto.getName());
            connection.setDescription(dto.getDescription());
            connection.setSshKey(dto.getSshKey());
            connection.setSshHost(dto.getSshHost());
            return connection;
        } else {
            Connection connection = new Connection();
            if (id != null) {
                connection.setId(id);
            }
            connection.setName(dto.getName());
            connection.setDescription(dto.getDescription());
            connection.setPbsHost(dto.getPbsHost());
            return connection;
        }
    }

    public Connection fromDto(com.pbs.middleware.api.connection.Connection dto) {
        if (dto.getType() == PASSWORD_AUTH) {
            PasswordConnection connection = new PasswordConnection();
            connection.setId(dto.getId());
            connection.setName(dto.getName());
            connection.setDescription(dto.getDescription());
            connection.setPbsHost(dto.getPbsHost());
            connection.setLogin(dto.getLogin());
            connection.setPassword(dto.getPassword());
            connection.setSshHost(dto.getSshHost());
            return connection;
        } else if (dto.getType() == SSH_KEY_AUTH) {
            SshKeyConnection connection = new SshKeyConnection();
            connection.setId(dto.getId());
            connection.setName(dto.getName());
            connection.setDescription(dto.getDescription());
            connection.setPbsHost(dto.getPbsHost());
            connection.setSshKey(dto.getSshKey());
            connection.setSshHost(dto.getSshHost());
            return connection;
        } else {
            Connection connection = new Connection();
            connection.setId(dto.getId());
            connection.setName(dto.getName());
            connection.setDescription(dto.getDescription());
            connection.setPbsHost(dto.getPbsHost());
            return connection;
        }
    }

}
