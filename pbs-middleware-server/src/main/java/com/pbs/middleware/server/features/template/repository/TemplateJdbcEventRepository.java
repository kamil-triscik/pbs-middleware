package com.pbs.middleware.server.features.template.repository;


import com.pbs.middleware.server.common.storage.event.jdbc.JdbcDomainIdMapper;
import com.pbs.middleware.server.common.storage.event.jdbc.JdbcEnabled;
import com.pbs.middleware.server.common.storage.event.jdbc.JdbcEventMapper;
import com.pbs.middleware.server.common.storage.event.jdbc.JdbcEventRepository;
import com.pbs.middleware.server.features.template.events.TemplateEvent;
import lombok.NonNull;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@JdbcEnabled
@Component
public class TemplateJdbcEventRepository extends JdbcEventRepository<TemplateEvent, String> {

    private static final String TEMPLATE_TABLE_NAME = "template";

    public TemplateJdbcEventRepository(
            @NonNull NamedParameterJdbcTemplate jdbcTemplate,
            @NonNull JdbcDomainIdMapper<String> domainIdMapper,
            @NonNull JdbcEventMapper<TemplateEvent> eventMapper) {
        super(jdbcTemplate, domainIdMapper, eventMapper, TEMPLATE_TABLE_NAME);
    }
}
