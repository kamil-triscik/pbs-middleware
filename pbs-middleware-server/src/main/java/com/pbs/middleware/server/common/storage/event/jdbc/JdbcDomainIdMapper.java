package com.pbs.middleware.server.common.storage.event.jdbc;

import org.springframework.jdbc.core.RowMapper;

public interface JdbcDomainIdMapper<ID> extends RowMapper<ID> {

    Object toParameter(ID id);

    ID toID(String id);

}
