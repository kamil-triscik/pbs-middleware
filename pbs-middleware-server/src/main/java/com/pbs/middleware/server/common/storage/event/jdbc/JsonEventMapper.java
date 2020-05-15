package com.pbs.middleware.server.common.storage.event.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.server.common.domain.MiddlewareEvent;
import com.pbs.middleware.server.common.storage.event.StorageException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.common.storage.event.jdbc.JdbcEventRepository.EVENT_BODY_COLUMN_NAME;
import static com.pbs.middleware.server.common.storage.event.jdbc.JdbcEventRepository.EVENT_TYPE_COLUMN_NAME;

@Component
@JdbcEnabled
@RequiredArgsConstructor
final public class JsonEventMapper<EVENT extends MiddlewareEvent> implements JdbcEventMapper<EVENT> {

    @NonNull
    private ObjectMapper objectMapper;

    @Override
    public Object toParameter(EVENT event) throws StorageException {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException ex) {
            throw new StorageException(ex);
        }
    }

    @Override
    public EVENT toEvent(ResultSet resultSet) throws StorageException {
        try {
            return (EVENT) objectMapper.readValue(resultSet.getString(EVENT_BODY_COLUMN_NAME),
                    Class.forName(resultSet.getString(EVENT_TYPE_COLUMN_NAME)));
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            throw new StorageException(ex);
        }
    }

    @Override
    public EVENT mapRow(ResultSet resultSet, int i) throws SQLException {
        try { //todo
            return (EVENT) objectMapper.readValue(resultSet.getString(EVENT_BODY_COLUMN_NAME),
                    Class.forName(resultSet.getString(EVENT_TYPE_COLUMN_NAME)));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}