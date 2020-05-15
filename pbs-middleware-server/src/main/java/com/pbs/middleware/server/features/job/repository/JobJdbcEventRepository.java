package com.pbs.middleware.server.features.job.repository;


import com.pbs.middleware.server.common.storage.event.jdbc.JdbcDomainIdMapper;
import com.pbs.middleware.server.common.storage.event.jdbc.JdbcEnabled;
import com.pbs.middleware.server.common.storage.event.jdbc.JdbcEventMapper;
import com.pbs.middleware.server.common.storage.event.jdbc.JdbcEventRepository;
import com.pbs.middleware.server.features.job.events.JobEvent;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@JdbcEnabled
@Component
public class JobJdbcEventRepository extends JdbcEventRepository<JobEvent, UUID> {

    private static final String JOB_TABLE_NAME = "job";

    public JobJdbcEventRepository(
            @NonNull NamedParameterJdbcTemplate jdbcTemplate,
            @NonNull JdbcDomainIdMapper<UUID> domainIdMapper,
            @NonNull JdbcEventMapper<JobEvent> eventMapper) {
        super(jdbcTemplate, domainIdMapper, eventMapper, JOB_TABLE_NAME);
    }
}
