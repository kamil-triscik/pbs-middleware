package com.pbs.middleware.server.common.storage.event.jdbc;

import com.pbs.middleware.server.common.domain.Id;
import com.pbs.middleware.server.common.domain.MiddlewareEvent;
import com.pbs.middleware.server.common.storage.EventRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RequiredArgsConstructor
public abstract class JdbcEventRepository<EVENT extends MiddlewareEvent<ID>, ID> implements EventRepository<EVENT, ID> {

    public final static String DOMAIN_ID_COLUMN_NAME = "domain_id";
    private final static String EVENT_UUID_COLUMN_NAME = "event_uuid";
    public final static String EVENT_TYPE_COLUMN_NAME = "event_type";
    private final static String EVENT_TIME_COLUMN_NAME = "event_time";
    public final static String EVENT_BODY_COLUMN_NAME = "event_body";

    @NonNull
    private final NamedParameterJdbcTemplate jdbcTemplate;
    @NonNull
    private final JdbcDomainIdMapper<ID> domainIdMapper;
    @NonNull
    private final JdbcEventMapper<EVENT> eventMapper;
    @NonNull
    private final String tableName;

    @Override
    public List<EVENT> findAll() {
        String sql = "SELECT " + EVENT_TYPE_COLUMN_NAME + ", " + EVENT_BODY_COLUMN_NAME
                + " FROM public." + this.tableName
                + " ORDER BY " + EVENT_TIME_COLUMN_NAME
                + " ASC ";

        return jdbcTemplate.query(sql, eventMapper);
    }

    @Override
    public Set<Id<ID>> findAllIds() {
        String sql = "SELECT " + DOMAIN_ID_COLUMN_NAME
                + " FROM public." + this.tableName
                + " ORDER BY " + EVENT_TIME_COLUMN_NAME
                + " ASC ";

        return jdbcTemplate.query(sql, domainIdMapper)
                .stream()
                .map(it -> new Id<ID>() {{
                    setDomainId(it);
                }})
                .collect(Collectors.toSet());
    }


    @Override
    public List<EVENT> findAllByDomainId(ID domainId) {
        return findAllByDomainId(domainId, Integer.MAX_VALUE);
    }

    public List<EVENT> findAllByDomainId(ID domainId, Integer limit) {
        //todo sql injection
        String sql = "SELECT " + EVENT_TYPE_COLUMN_NAME + ", " + EVENT_BODY_COLUMN_NAME
                + " FROM public." + this.tableName
                + " WHERE " + DOMAIN_ID_COLUMN_NAME + " = :" + DOMAIN_ID_COLUMN_NAME
                + " ORDER BY " + EVENT_TIME_COLUMN_NAME
                + " ASC " + limit;

        Map<String, Object> sqlMapping = Collections.singletonMap(DOMAIN_ID_COLUMN_NAME, domainIdMapper.toParameter(domainId));

        return jdbcTemplate.query(sql, sqlMapping, eventMapper);
    }

    @Override
    public void deleteAllByDomainId(Object domainId) {
        jdbcTemplate.update("DELETE FROM public." + this.tableName + " WHERE " + DOMAIN_ID_COLUMN_NAME + " = :id; ",
                new MapSqlParameterSource("id", domainId));
    }

    @Override
    public EVENT save(EVENT event) {
        String sql = "INSERT INTO public." + this.tableName + " (" +
                JdbcEventRepository.EVENT_UUID_COLUMN_NAME + ", " +
                JdbcEventRepository.EVENT_TYPE_COLUMN_NAME + ", " +
                JdbcEventRepository.EVENT_TIME_COLUMN_NAME + ", " +
                JdbcEventRepository.EVENT_BODY_COLUMN_NAME + ", " +
                JdbcEventRepository.DOMAIN_ID_COLUMN_NAME + ") " +
                "VALUES (:uuid, :type, :time, (:body::JSON), :domain_id)";
        Map<String, Object> sqlMapping = new HashMap<>() {{
            put("uuid", event.getEventId());
            put("type", event.getClass().getName());
            put("time", event.getInstant().toEpochMilli());
            put("body", eventMapper.toParameter(event));
            put("domain_id", domainIdMapper.toParameter(event.getDomainId()));
        }};

        jdbcTemplate.update(sql, sqlMapping);

        return event;
    }
}
