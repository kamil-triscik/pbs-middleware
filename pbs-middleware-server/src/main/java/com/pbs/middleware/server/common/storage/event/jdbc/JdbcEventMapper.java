package com.pbs.middleware.server.common.storage.event.jdbc;

import com.pbs.middleware.server.common.domain.MiddlewareEvent;
import com.pbs.middleware.server.common.storage.event.StorageException;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;

public interface JdbcEventMapper<EVENT extends MiddlewareEvent> extends RowMapper<EVENT> {

    Object toParameter(EVENT event) throws StorageException;

    EVENT toEvent(ResultSet resultSet) throws StorageException;

}
