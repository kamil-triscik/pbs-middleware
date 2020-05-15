package com.pbs.middleware.server.features.connection.service;

import com.pbs.middleware.api.connection.CreateConnection;
import com.pbs.middleware.api.connection.ConnectionType;
import com.pbs.middleware.api.connection.UpdateConnection;
import com.pbs.middleware.server.features.connection.repository.Connection;
import com.pbs.middleware.server.features.connection.repository.PasswordConnection;
import com.pbs.middleware.server.features.connection.repository.SshKeyConnection;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Connection mapper tests")
class ConnectionMapperTest {

    @Test
    @DisplayName("toDto(): mapping LocalConnection object to Dto")
    void toDtoFromLocalConnectionTest() {
        final UUID id = UUID.randomUUID();
        final String pbsHost = "somePbsHot";
        final String name = "name";
        final String desc = "desc";

        Connection connection = new Connection();
        connection.setId(id);
        connection.setName(name);
        connection.setDescription(desc);
        connection.setPbsHost(pbsHost);

        com.pbs.middleware.api.connection.Connection dto = new ConnectionMapper().toDto(connection);

        assertNotNull(dto.getId(), "DTO does not have an ID");
        assertEquals(id, dto.getId(), "DTO has wrong ID");
        assertNotNull(dto.getName(), "DTO does not have a name");
        assertEquals(name, dto.getName(), "DTO has wrong name");
        assertNotNull(dto.getDescription(), "DTO does not have a description");
        assertEquals(desc, dto.getDescription(), "DTO has wrong desc");
        assertNotNull(dto.getPbsHost(), "DTO does not have the pbs host");
        assertEquals(pbsHost, dto.getPbsHost(), "DTO has wrong pbs host");
        assertNotNull(dto.getType(), "DTO does not have the connection type");
        Assertions.assertEquals(ConnectionType.LOCAL, dto.getType(), "DTO has wrong type");
        assertNull(dto.getSshHost(), "SshHost should be NULL");
        assertNull(dto.getLogin(), "Login should be NULL");
        assertNull(dto.getPassword(), "Password should be NULL");
        assertNull(dto.getSshKey(), "SSH key should be NULL");
    }

    @Test
    @DisplayName("toDto(): mapping PasswordConnection object to Dto")
    void toDtoFromPasswordConnectionTest() {
        final UUID id = UUID.randomUUID();
        final String pbsHost = "somePbsHot";
        final String sshHost = "someSshHost";
        final String name = "name";
        final String desc = "desc";
        final String login = "login";
        final String password = "password";

        PasswordConnection connection = new PasswordConnection();
        connection.setId(id);
        connection.setName(name);
        connection.setDescription(desc);
        connection.setPbsHost(pbsHost);
        connection.setSshHost(sshHost);
        connection.setLogin(login);
        connection.setPassword(password);

        com.pbs.middleware.api.connection.Connection dto = new ConnectionMapper().toDto(connection);

        assertNotNull(dto.getId(), "DTO does not have an ID");
        assertEquals(id, dto.getId(), "DTO has wrong ID");
        assertNotNull(dto.getName(), "DTO does not have a name");
        assertEquals(name, dto.getName(), "DTO has wrong name");
        assertNotNull(dto.getDescription(), "DTO does not have a description");
        assertEquals(desc, dto.getDescription(), "DTO has wrong desc");
        assertNotNull(dto.getPbsHost(), "DTO does not have the pbs host");
        assertEquals(pbsHost, dto.getPbsHost(), "DTO has wrong pbs host");
        assertNotNull(dto.getType(), "DTO does not have the connection type");
        Assertions.assertEquals(ConnectionType.PASSWORD_AUTH, dto.getType(), "DTO has wrong type");
        assertNotNull(dto.getSshHost(), "SshHost should not be NULL");
        assertEquals(sshHost, dto.getSshHost(), "DTO has wrong ssh host");
        assertNotNull(dto.getLogin(), "Login should not be NULL");
        assertEquals(login, dto.getLogin(), "DTO has wrong login");
        assertNotNull(dto.getPassword(), "Password should not be NULL");
        assertEquals(password, dto.getPassword(), "DTO has wrong password");
        assertNull(dto.getSshKey(), "SSH key should be NULL");
    }

    @Test
    @DisplayName("toDto(): mapping SshKeyAuthConnection object to Dto")
    void toDtoFromSshKeyAuthConnectionTest() {
        final UUID id = UUID.randomUUID();
        final String pbsHost = "somePbsHot";
        final String sshHost = "someSshHost";
        final String name = "name";
        final String desc = "desc";
        final String sshKey = "sshKey";

        SshKeyConnection connection = new SshKeyConnection();
        connection.setId(id);
        connection.setName(name);
        connection.setDescription(desc);
        connection.setPbsHost(pbsHost);
        connection.setSshHost(sshHost);
        connection.setSshKey(sshKey);

        com.pbs.middleware.api.connection.Connection dto = new ConnectionMapper().toDto(connection);

        assertNotNull(dto.getId(), "DTO does not have an ID");
        assertEquals(id, dto.getId(), "DTO has wrong ID");
        assertNotNull(dto.getName(), "DTO does not have a name");
        assertEquals(name, dto.getName(), "DTO has wrong name");
        assertNotNull(dto.getDescription(), "DTO does not have a description");
        assertEquals(desc, dto.getDescription(), "DTO has wrong desc");
        assertNotNull(dto.getPbsHost(), "DTO does not have the pbs host");
        assertEquals(pbsHost, dto.getPbsHost(), "DTO has wrong pbs host");
        assertNotNull(dto.getType(), "DTO does not have the connection type");
        Assertions.assertEquals(ConnectionType.SSH_KEY_AUTH, dto.getType(), "DTO has wrong type");
        assertNotNull(dto.getSshHost(), "SshHost should not be NULL");
        assertEquals(sshHost, dto.getSshHost(), "DTO has wrong ssh host");
        assertNull(dto.getLogin(), "Login should be NULL");
        assertNull(dto.getPassword(), "Password should be NULL");
        assertNotNull(dto.getSshKey(), "SSH key should not be NULL");
        assertEquals(sshKey, dto.getSshKey(), "DTO has wrong ssh key");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionCreateDto with id object to LocalConnection")
    void fromConnectionCreateDtoWithIdToLocalConnectionTest() {
        final UUID id = UUID.randomUUID();
        final String pbsHost = "somePbsHot";
        final String name = "name";
        final String desc = "desc";

        CreateConnection dto = new CreateConnection();
        dto.setType(ConnectionType.LOCAL);
        dto.setPbsHost(pbsHost);
        dto.setName(name);
        dto.setDescription(desc);

        Object connectionObject = new ConnectionMapper().fromDto(id, dto);

        assertTrue(connectionObject instanceof Connection, "ConnectionObject should be instance of LocalConnection");
        assertFalse(connectionObject instanceof PasswordConnection, "Connection should not be instance of PasswordConnection");
        assertFalse(connectionObject instanceof SshKeyConnection, "Connection should not be instance of SshKeyAuthConnection");
        Connection connection = (Connection) connectionObject;

        assertNotNull(connection.getId(), "Connection does not have an ID");
        assertEquals(id, connection.getId(), "Connection has wrong ID");
        assertNotNull(connection.getName(), "Connection does not have a name");
        assertEquals(name, connection.getName(), "Connection has wrong name");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionCreateDto without id object to LocalConnection")
    void fromConnectionCreateDtoWithoutIdToLocalConnectionTest() {
        final String pbsHost = "somePbsHot";
        final String name = "name";
        final String desc = "desc";

        CreateConnection dto = new CreateConnection();
        dto.setType(ConnectionType.LOCAL);
        dto.setPbsHost(pbsHost);
        dto.setName(name);
        dto.setDescription(desc);

        Object connectionObject = new ConnectionMapper().fromDto(dto);

        assertTrue(connectionObject instanceof Connection, "ConnectionObject should be instance of LocalConnection");
        assertFalse(connectionObject instanceof PasswordConnection, "Connection should not be instance of PasswordConnection");
        assertFalse(connectionObject instanceof SshKeyConnection, "Connection should not be instance of SshKeyAuthConnection");
        Connection connection = (Connection) connectionObject;

        assertNull(connection.getId(), "Connection ID should be NULL");
        assertNotNull(connection.getName(), "Connection does not have a name");
        assertEquals(name, connection.getName(), "Connection has wrong name");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionCreateDto with id object to PasswordConnection")
    void fromConnectionCreateDtoWithIdToCPasswordConnectionTest() {
        final UUID id = UUID.randomUUID();
        final String pbsHost = "somePbsHot";
        final String sshHost = "someSshHost";
        final String name = "name";
        final String desc = "desc";
        final String login = "login";
        final String password = "password";

        CreateConnection dto = new CreateConnection();
        dto.setType(ConnectionType.PASSWORD_AUTH);
        dto.setPbsHost(pbsHost);
        dto.setSshHost(sshHost);
        dto.setName(name);
        dto.setDescription(desc);
        dto.setLogin(login);
        dto.setPassword(password);

        Object connectionObject = new ConnectionMapper().fromDto(id, dto);

        assertTrue(connectionObject instanceof PasswordConnection, "Connection should be instance of PasswordConnection");
        PasswordConnection connection = (PasswordConnection) connectionObject;

        assertNotNull(connection.getId(), "Connection does not have an ID");
        assertEquals(id, connection.getId(), "Connection has wrong ID");
        assertNotNull(connection.getName(), "Connection does not have a name");
        assertEquals(name, connection.getName(), "Connection has wrong name");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
        assertNotNull(connection.getSshHost(), "SshHost should not be NULL");
        assertEquals(sshHost, connection.getSshHost(), "Connection has wrong ssh host");
        assertNotNull(connection.getLogin(), "Login should not be NULL");
        assertEquals(login, connection.getLogin(), "Connection has wrong login");
        assertNotNull(connection.getPassword(), "Password should not be NULL");
        assertEquals(password, connection.getPassword(), "Connection has wrong password");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionCreateDto without id object to PasswordConnection")
    void fromConnectionCreateDtoWithoutIdToPasswordConnectionTest() {
        final String pbsHost = "somePbsHot";
        final String sshHost = "someSshHost";
        final String name = "name";
        final String desc = "desc";
        final String login = "login";
        final String password = "password";

        CreateConnection dto = new CreateConnection();
        dto.setType(ConnectionType.PASSWORD_AUTH);
        dto.setPbsHost(pbsHost);
        dto.setSshHost(sshHost);
        dto.setName(name);
        dto.setDescription(desc);
        dto.setLogin(login);
        dto.setPassword(password);

        Object connectionObject = new ConnectionMapper().fromDto(dto);

        assertTrue(connectionObject instanceof PasswordConnection, "Connection should be instance of PasswordConnection");
        PasswordConnection connection = (PasswordConnection) connectionObject;

        assertNull(connection.getId(), "Connection ID should be NULL");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertNotNull(connection.getName(), "Connection does not have a name");
        assertEquals(name, connection.getName(), "Connection has wrong name");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
        assertNotNull(connection.getSshHost(), "SshHost should not be NULL");
        assertEquals(sshHost, connection.getSshHost(), "Connection has wrong ssh host");
        assertNotNull(connection.getLogin(), "Login should not be NULL");
        assertEquals(login, connection.getLogin(), "Connection has wrong login");
        assertNotNull(connection.getPassword(), "Password should not be NULL");
        assertEquals(password, connection.getPassword(), "Connection has wrong password");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionCreateDto with id object to SshKeyAuthConnection")
    void fromConnectionCreateDtoWithIdToSshKeyAuthConnectionTest() {
        final UUID id = UUID.randomUUID();
        final String pbsHost = "somePbsHot";
        final String sshHost = "someSshHost";
        final String name = "name";
        final String desc = "desc";
        final String sshKey = "someSshKey";

        CreateConnection dto = new CreateConnection();
        dto.setType(ConnectionType.SSH_KEY_AUTH);
        dto.setName(name);
        dto.setDescription(desc);
        dto.setPbsHost(pbsHost);
        dto.setSshHost(sshHost);
        dto.setSshKey(sshKey);

        Object connectionObject = new ConnectionMapper().fromDto(id, dto);

        assertTrue(connectionObject instanceof SshKeyConnection, "Connection should be instance of SshKeyAuthConnection");
        SshKeyConnection connection = (SshKeyConnection) connectionObject;

        assertNotNull(connection.getId(), "Connection does not have an ID");
        assertEquals(id, connection.getId(), "Connection has wrong ID");
        assertNotNull(connection.getName(), "Connection does not have a name");
        assertEquals(name, connection.getName(), "Connection has wrong name");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
        assertNotNull(connection.getSshHost(), "Connection does not have the ssh host");
        assertEquals(sshHost, connection.getSshHost(), "Connection has wrong ssh host");
        assertNotNull(connection.getSshKey(), "Connection does not have the ssh key");
        assertEquals(sshKey, connection.getSshKey(), "Connection has wrong ssh key");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionCreateDto without id object to SshKeyAuthConnection")
    void fromConnectionCreateDtoWithoutIdToSshKeyAuthConnectionTest() {
        final String pbsHost = "somePbsHost";
        final String sshHost = "someSshHost";
        final String sshKey = "someSshKey";
        final String name = "name";
        final String desc = "desc";

        CreateConnection dto = new CreateConnection();
        dto.setType(ConnectionType.SSH_KEY_AUTH);
        dto.setName(name);
        dto.setDescription(desc);
        dto.setPbsHost(pbsHost);
        dto.setSshHost(sshHost);
        dto.setSshKey(sshKey);

        Object connectionObject = new ConnectionMapper().fromDto(dto);

        assertTrue(connectionObject instanceof SshKeyConnection, "Connection should be instance of SshKeyAuthConnection");
        SshKeyConnection connection = (SshKeyConnection) connectionObject;

        assertNull(connection.getId(), "Connection ID should be NULL");
        assertNotNull(connection.getName(), "Connection does not have a name");
        assertEquals(name, connection.getName(), "Connection has wrong name");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
        assertNotNull(connection.getSshHost(), "Connection does not have the ssh host");
        assertEquals(sshHost, connection.getSshHost(), "Connection has wrong ssh host");
        assertNotNull(connection.getSshKey(), "Connection does not have the ssh key");
        assertEquals(sshKey, connection.getSshKey(), "Connection has wrong ssh key");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionUpdateDto with id object to LocalConnection")
    void fromConnectionUpdateDtoWithIdToLocalConnectionTest() {
        final UUID id = UUID.randomUUID();
        final String pbsHost = "somePbsHot";
        final String desc = "desc";

        UpdateConnection dto = new UpdateConnection();
        dto.setType(ConnectionType.LOCAL);
        dto.setPbsHost(pbsHost);
        dto.setDescription(desc);

        Object connectionObject = new ConnectionMapper().fromDto(id, dto);

        assertTrue(connectionObject instanceof Connection, "ConnectionObject should be instance of LocalConnection");
        assertFalse(connectionObject instanceof PasswordConnection, "Connection should not be instance of PasswordConnection");
        assertFalse(connectionObject instanceof SshKeyConnection, "Connection should not be instance of SshKeyAuthConnection");
        Connection connection = (Connection) connectionObject;

        assertNotNull(connection.getId(), "Connection does not have an ID");
        assertEquals(id, connection.getId(), "Connection has wrong ID");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionUpdateDto without id object to LocalConnection")
    void fromConnectionUpdateDtoWithoutIdToLocalConnectionTest() {
        final String pbsHost = "somePbsHot";
        final String desc = "desc";

        UpdateConnection dto = new UpdateConnection();
        dto.setType(ConnectionType.LOCAL);
        dto.setPbsHost(pbsHost);
        dto.setDescription(desc);

        Object connectionObject = new ConnectionMapper().fromDto(dto);

        assertTrue(connectionObject instanceof Connection, "ConnectionObject should be instance of LocalConnection");
        assertFalse(connectionObject instanceof PasswordConnection, "Connection should not be instance of PasswordConnection");
        assertFalse(connectionObject instanceof SshKeyConnection, "Connection should not be instance of SshKeyAuthConnection");
        Connection connection = (Connection) connectionObject;

        assertNull(connection.getId(), "Connection ID should be NULL");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionUpdateDto with id object to PasswordConnection")
    void fromConnectionUpdateDtoWithIdToCPasswordConnectionTest() {
        final UUID id = UUID.randomUUID();
        final String pbsHost = "somePbsHot";
        final String sshHost = "someSshHost";
        final String login = "login";
        final String password = "password";
        final String desc = "desc";

        UpdateConnection dto = new UpdateConnection();
        dto.setType(ConnectionType.PASSWORD_AUTH);
        dto.setPbsHost(pbsHost);
        dto.setSshHost(sshHost);
        dto.setLogin(login);
        dto.setPassword(password);
        dto.setDescription(desc);

        Object connectionObject = new ConnectionMapper().fromDto(id, dto);

        assertTrue(connectionObject instanceof PasswordConnection, "Connection should be instance of PasswordConnection");
        PasswordConnection connection = (PasswordConnection) connectionObject;

        assertNotNull(connection.getId(), "Connection does not have an ID");
        assertEquals(id, connection.getId(), "Connection has wrong ID");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
        assertNotNull(connection.getSshHost(), "SshHost should not be NULL");
        assertEquals(sshHost, connection.getSshHost(), "Connection has wrong ssh host");
        assertNotNull(connection.getLogin(), "Login should not be NULL");
        assertEquals(login, connection.getLogin(), "Connection has wrong login");
        assertNotNull(connection.getPassword(), "Password should not be NULL");
        assertEquals(password, connection.getPassword(), "Connection has wrong password");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionUpdateDto without id object to PasswordConnection")
    void fromConnectionUpdateDtoWithoutIdToPasswordConnectionTest() {
        final String pbsHost = "somePbsHot";
        final String sshHost = "someSshHost";
        final String login = "login";
        final String password = "password";
        final String desc = "desc";

        UpdateConnection dto = new UpdateConnection();
        dto.setType(ConnectionType.PASSWORD_AUTH);
        dto.setPbsHost(pbsHost);
        dto.setSshHost(sshHost);
        dto.setLogin(login);
        dto.setPassword(password);
        dto.setDescription(desc);

        Object connectionObject = new ConnectionMapper().fromDto(dto);

        assertTrue(connectionObject instanceof PasswordConnection, "Connection should be instance of PasswordConnection");
        PasswordConnection connection = (PasswordConnection) connectionObject;

        assertNull(connection.getId(), "Connection ID should be NULL");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
        assertNotNull(connection.getSshHost(), "SshHost should not be NULL");
        assertEquals(sshHost, connection.getSshHost(), "Connection has wrong ssh host");
        assertNotNull(connection.getLogin(), "Login should not be NULL");
        assertEquals(login, connection.getLogin(), "Connection has wrong login");
        assertNotNull(connection.getPassword(), "Password should not be NULL");
        assertEquals(password, connection.getPassword(), "Connection has wrong password");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionUpdateDto with id object to SshKeyAuthConnection")
    void fromConnectionUpdateDtoWithIdToSshKeyAuthConnectionTest() {
        final UUID id = UUID.randomUUID();
        final String pbsHost = "somePbsHot";
        final String sshHost = "someSshHost";
        final String sshKey = "someSshKey";
        final String desc = "desc";

        UpdateConnection dto = new UpdateConnection();
        dto.setType(ConnectionType.SSH_KEY_AUTH);
        dto.setPbsHost(pbsHost);
        dto.setSshHost(sshHost);
        dto.setSshKey(sshKey);
        dto.setDescription(desc);

        Object connectionObject = new ConnectionMapper().fromDto(id, dto);

        assertTrue(connectionObject instanceof SshKeyConnection, "Connection should be instance of SshKeyAuthConnection");
        SshKeyConnection connection = (SshKeyConnection) connectionObject;

        assertNotNull(connection.getId(), "Connection does not have an ID");
        assertEquals(id, connection.getId(), "Connection has wrong ID");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
        assertNotNull(connection.getSshHost(), "Connection does not have the ssh host");
        assertEquals(sshHost, connection.getSshHost(), "Connection has wrong ssh host");
        assertNotNull(connection.getSshKey(), "Connection does not have the ssh key");
        assertEquals(sshKey, connection.getSshKey(), "Connection has wrong ssh key");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionUpdateDto without id object to SshKeyAuthConnection")
    void fromConnectionUpdateDtoWithoutIdToSshKeyAuthConnectionTest() {
        final String pbsHost = "somePbsHost";
        final String sshHost = "someSshHost";
        final String sshKey = "someSshKey";
        final String desc = "desc";

        UpdateConnection dto = new UpdateConnection();
        dto.setType(ConnectionType.SSH_KEY_AUTH);
        dto.setPbsHost(pbsHost);
        dto.setSshHost(sshHost);
        dto.setSshKey(sshKey);
        dto.setDescription(desc);

        Object connectionObject = new ConnectionMapper().fromDto(dto);

        assertTrue(connectionObject instanceof SshKeyConnection, "Connection should be instance of SshKeyAuthConnection");
        SshKeyConnection connection = (SshKeyConnection) connectionObject;

        assertNull(connection.getId(), "Connection ID should be NULL");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
        assertNotNull(connection.getSshHost(), "Connection does not have the ssh host");
        assertEquals(sshHost, connection.getSshHost(), "Connection has wrong ssh host");
        assertNotNull(connection.getSshKey(), "Connection does not have the ssh key");
        assertEquals(sshKey, connection.getSshKey(), "Connection has wrong ssh key");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionDto object to LocalConnection")
    void fromConnectionDtoToLocalConnectionTest() {
        final UUID id = UUID.randomUUID();
        final String pbsHost = "somePbsHot";
        final String name = "name";
        final String desc = "desc";

        com.pbs.middleware.api.connection.Connection dto = new com.pbs.middleware.api.connection.Connection();
        dto.setId(id);
        dto.setType(ConnectionType.LOCAL);
        dto.setName(name);
        dto.setDescription(desc);
        dto.setPbsHost(pbsHost);

        Object connectionObject = new ConnectionMapper().fromDto(dto);

        assertTrue(connectionObject instanceof Connection, "ConnectionObject should be instance of LocalConnection");
        assertFalse(connectionObject instanceof PasswordConnection, "Connection should not be instance of PasswordConnection");
        assertFalse(connectionObject instanceof SshKeyConnection, "Connection should not be instance of SshKeyAuthConnection");
        Connection connection = (Connection) connectionObject;

        assertNotNull(connection.getId(), "Connection ID should be NULL");
        assertNotNull(connection.getName(), "Connection does not have a name");
        assertEquals(name, connection.getName(), "Connection has wrong name");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");

        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionDto object to PasswordConnection")
    void fromConnectionDtoToPasswordConnectionTest() {
        final UUID id = UUID.randomUUID();
        final String pbsHost = "somePbsHot";
        final String sshHost = "someSshHost";
        final String name = "name";
        final String desc = "desc";
        final String login = "login";
        final String password = "password";

        com.pbs.middleware.api.connection.Connection dto = new com.pbs.middleware.api.connection.Connection();
        dto.setId(id);
        dto.setType(ConnectionType.PASSWORD_AUTH);
        dto.setPbsHost(pbsHost);
        dto.setSshHost(sshHost);
        dto.setName(name);
        dto.setDescription(desc);
        dto.setLogin(login);
        dto.setPassword(password);

        Object connectionObject = new ConnectionMapper().fromDto(dto);

        assertTrue(connectionObject instanceof PasswordConnection, "Connection should be instance of PasswordConnection");
        PasswordConnection connection = (PasswordConnection) connectionObject;

        assertNotNull(connection.getId(), "Connection does not have an ID");
        assertEquals(id, connection.getId(), "Connection has wrong ID");
        assertNotNull(connection.getName(), "Connection does not have a name");
        assertEquals(name, connection.getName(), "Connection has wrong name");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
        assertNotNull(connection.getSshHost(), "SshHost should not be NULL");
        assertEquals(sshHost, connection.getSshHost(), "Connection has wrong ssh host");
        assertNotNull(connection.getLogin(), "Login should not be NULL");
        assertEquals(login, connection.getLogin(), "Connection has wrong login");
        assertNotNull(connection.getPassword(), "Password should not be NULL");
        assertEquals(password, connection.getPassword(), "Connection has wrong password");
    }

    @Test
    @DisplayName("fromDto(): mapping ConnectionDto object to SshKeyAuthConnection")
    void fromConnectionDtoToSshKeyAuthConnectionTest() {
        final UUID id = UUID.randomUUID();
        final String pbsHost = "somePbsHot";
        final String sshHost = "someSshHost";
        final String sshKey = "someSshKey";
        final String name = "name";
        final String desc = "desc";

        com.pbs.middleware.api.connection.Connection dto = new com.pbs.middleware.api.connection.Connection();
        dto.setId(id);
        dto.setType(ConnectionType.SSH_KEY_AUTH);
        dto.setPbsHost(pbsHost);
        dto.setName(name);
        dto.setDescription(desc);
        dto.setSshHost(sshHost);
        dto.setSshKey(sshKey);

        Object connectionObject = new ConnectionMapper().fromDto(dto);

        assertTrue(connectionObject instanceof SshKeyConnection, "Connection should be instance of SshKeyAuthConnection");
        SshKeyConnection connection = (SshKeyConnection) connectionObject;

        assertNotNull(connection.getId(), "Connection does not have an ID");
        assertEquals(id, connection.getId(), "Connection has wrong ID");
        assertNotNull(connection.getName(), "Connection does not have a name");
        assertEquals(name, connection.getName(), "Connection has wrong name");
        assertNotNull(connection.getDescription(), "Connection does not have a description");
        assertEquals(desc, connection.getDescription(), "Connection has wrong desc");
        assertNotNull(connection.getPbsHost(), "Connection does not have the pbs host");
        assertEquals(pbsHost, connection.getPbsHost(), "Connection has wrong pbs host");
        assertNotNull(connection.getSshHost(), "Connection does not have the ssh host");
        assertEquals(sshHost, connection.getSshHost(), "Connection has wrong ssh host");
        assertNotNull(connection.getSshKey(), "Connection does not have the ssh key");
        assertEquals(sshKey, connection.getSshKey(), "Connection has wrong ssh key");
    }

}