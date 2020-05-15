package com.pbs.middleware.server.features.connection.service;

import com.pbs.middleware.server.features.connection.repository.ConnectionRepository;
import com.pbs.middleware.server.features.connection.repository.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Connection service tests")
class ConnectionServiceTest {

    @Captor
    ArgumentCaptor<UUID> idCaptor;

    @Captor
    ArgumentCaptor<String> nameCaptor;

    @Captor
    ArgumentCaptor<Connection> connectionCaptor;

    @Mock
    ConnectionRepository repository;

    @Test
    @DisplayName("get(): constructor test")
    void constructor_test() {
        assertThrows(
                NullPointerException.class,
                () -> new ConnectionService(null));
    }

    @Test
    @DisplayName("getAll(): test")
    void get_all_test() {
        final Connection connection = new Connection();

        List<Connection> allConnections = new ArrayList<>() {{
            add(connection);
        }};

        when(repository.findAll()).thenReturn(allConnections);

        List<Connection> connections = new ConnectionService(repository).getAll();

        assertEquals(1, connections.size());
        assertEquals(connection, connections.get(0));
        verify(repository, times(1)).findAll();

    }

    @Test
    @DisplayName("get(): id is null test")
    void get_with_null_argument_test() {
        assertThrows(
                NullPointerException.class,
                () -> new ConnectionService(repository).get((UUID) null));
    }

    @Test
    @DisplayName("get(): repository returned connection test")
    void get_returned_entity_test() {
        final Connection connection = new Connection();
        final UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.of(connection));

        Connection returnedConnection = new ConnectionService(repository).get(id);

        verify(repository).findById(idCaptor.capture());
        verify(repository, times(1)).findById(id);
        assertEquals(connection, returnedConnection);
        assertEquals(id, idCaptor.getValue());
    }

    @Test
    @DisplayName("update(): connection id is null string test")
    void get_null_string_test() {
        assertThrows(
                NullPointerException.class,
                () -> new ConnectionService(repository).get((String)null)
        );
    }

    @Test
    @DisplayName("update(): connection id is empty string test")
    void get_empty_string_test() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ConnectionService(repository).get("")
        );
    }

    @Test
    @DisplayName("update(): connection id is blank string test")
    void get_blank_string_test() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ConnectionService(repository).get("  ")
        );
    }

    @Test
    @DisplayName("get(): string repository returned connection for uuidOrName test")
    void get_uuid_or_name_returned_entity_test() {
        final Connection connection = new Connection();
        final UUID id = UUID.randomUUID();

        when(repository.findByNameOrUuid(eq(id), eq(id.toString()))).thenReturn(Optional.of(connection));

        Connection returnedConnection = new ConnectionService(repository).get(id.toString());

        verify(repository).findByNameOrUuid(idCaptor.capture(), nameCaptor.capture());
        verify(repository, times(1)).findByNameOrUuid(eq(id), eq(id.toString()));
        assertEquals(connection, returnedConnection);
        assertEquals(id, idCaptor.getValue());
        assertEquals(id.toString(), nameCaptor.getValue());
    }

    @Test
    @DisplayName("get(): string repository returned connection for uuidOrName not found test")
    void get_uuid_or_name_returned_entity_test_not_found() {
        final Connection connection = new Connection();
        final UUID id = UUID.randomUUID();

        when(repository.findByNameOrUuid(eq(id), eq(id.toString()))).thenReturn(Optional.empty());

        assertThrows(
                ConnectionNotFoundException.class,
                () -> new ConnectionService(repository).get(id.toString())
        );

        verify(repository).findByNameOrUuid(idCaptor.capture(), nameCaptor.capture());
        verify(repository, times(1)).findByNameOrUuid(eq(id), eq(id.toString()));
        assertEquals(id, idCaptor.getValue());
        assertEquals(id.toString(), nameCaptor.getValue());
    }

    @Test
    @DisplayName("get(): string repository returned connection for Name test")
    void get_for_name_returned_entity_test() {
        final Connection connection = new Connection();
        final String name = "conn_name";

        when(repository.findByName(name)).thenReturn(Optional.of(connection));

        Connection returnedConnection = new ConnectionService(repository).get(name);

        verify(repository).findByName(nameCaptor.capture());
        verify(repository, times(0)).findByNameOrUuid(any(), any());
        verify(repository, times(1)).findByName(name);
        assertEquals(connection, returnedConnection);
        assertEquals(name, nameCaptor.getValue());
    }

    @Test
    @DisplayName("get(): string repository not found for Name test")
    void get_for_name_not_found_test() {
        final String name = "conn_name";

        when(repository.findByName(name)).thenReturn(Optional.empty());

        assertThrows(
                ConnectionNotFoundException.class,
                () -> new ConnectionService(repository).get(name)
        );

        verify(repository).findByName(nameCaptor.capture());
        verify(repository, times(0)).findByNameOrUuid(any(), any());
        verify(repository, times(1)).findByName(name);
        assertEquals(name, nameCaptor.getValue());
    }

    @Test
    @DisplayName("create(): connection entity is null test")
    void create_connection_dto_is_null_test() {
        assertThrows(
                NullPointerException.class,
                () -> new ConnectionService(repository).create(null)
        );
    }

    @Test
    @DisplayName("create(): connection test")
    void create_connection_test() {
        final Connection connection = new Connection();

        when(repository.save(connection)).thenReturn(connection);

        new ConnectionService(repository).create(connection);

        verify(repository).save(connectionCaptor.capture());
        verify(repository, times(1)).save(connection);
        assertEquals(connection, connectionCaptor.getValue());
    }

    @Test
    @DisplayName("update(): connection id is null test")
    void update_connection_id_is_null_test() {
        assertThrows(
                NullPointerException.class,
                () -> new ConnectionService(repository).update(null, new Connection())
        );
    }

    @Test
    @DisplayName("update(): connection entity is null test")
    void update_connection_dto_is_null_test() {
        assertThrows(
                NullPointerException.class,
                () -> new ConnectionService(repository).update(UUID.randomUUID(), null)
        );
    }

    @Test
    @DisplayName("update(): existing connection test")
    void update_connection_test() {
        final UUID id = UUID.randomUUID();
        final Connection connection = new Connection();

        when(repository.save(connection)).thenReturn(connection);
        when(repository.findById(id)).thenReturn(Optional.of(connection));

        new ConnectionService(repository).update(id, connection);

        verify(repository).findById(idCaptor.capture());
        verify(repository).save(connectionCaptor.capture());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(connection);
        assertEquals(id, idCaptor.getValue());
        assertEquals(connection, connectionCaptor.getValue());
    }

    @Test
    @DisplayName("update(): not existing connection test")
    void update_not_existing_connection_test() {
        final UUID id = UUID.randomUUID();
        final Connection connection = new Connection();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                ConnectionNotFoundException.class,
                () -> new ConnectionService(repository).update(id, connection));

        verify(repository).findById(idCaptor.capture());
        verify(repository, times(1)).findById(id);
        verify(repository, times(0)).save(connection);
        assertEquals(id, idCaptor.getValue());
    }

    @Test
    @DisplayName("delete(): connection id is null test")
    void delete_connection_id_is_null_test() {
        assertThrows(
                NullPointerException.class,
                () -> new ConnectionService(repository).delete(null)
        );
    }

    @Test
    @DisplayName("delete(): delete test")
    void delete_test() {
        final UUID id = UUID.randomUUID();

        doNothing().when(repository).deleteById(id);

        new ConnectionService(repository).delete(id);

        verify(repository).deleteById(idCaptor.capture());
        verify(repository, times(1)).deleteById(id);
        assertEquals(id, idCaptor.getValue());
    }
}