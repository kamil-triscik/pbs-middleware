package com.pbs.middleware.server.common.storage.event.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.common.storage.event.jdbc.JdbcEventRepository.DOMAIN_ID_COLUMN_NAME;

@Component
@JdbcEnabled
final public class UuidDomainIdMapper implements JdbcDomainIdMapper<UUID> {

    @Override
    public Object toParameter(UUID uuid) {
        return uuid;
    }

    @Override
    public UUID toID(String id) {
        return UUID.fromString(id);
    }

    @Override
    public UUID mapRow(ResultSet resultSet, int i) throws SQLException {
        return UUID.fromString(resultSet.getString(DOMAIN_ID_COLUMN_NAME));
    }
}