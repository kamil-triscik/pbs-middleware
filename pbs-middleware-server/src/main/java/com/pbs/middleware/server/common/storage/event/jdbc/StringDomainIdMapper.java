package com.pbs.middleware.server.common.storage.event.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.common.storage.event.jdbc.JdbcEventRepository.DOMAIN_ID_COLUMN_NAME;

@Component
@JdbcEnabled
final public class StringDomainIdMapper implements JdbcDomainIdMapper<String> {

    @Override
    public Object toParameter(String id) {
        return id.toLowerCase();
    }

    @Override
    public String toID(String id) {
        return id;
    }

    @Override
    public String mapRow(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getString(DOMAIN_ID_COLUMN_NAME);
    }
}