package com.pbs.middleware.server.features.filetransfer.upload.repository;


import com.pbs.middleware.server.common.storage.event.jdbc.JdbcDomainIdMapper;
import com.pbs.middleware.server.common.storage.event.jdbc.JdbcEnabled;
import com.pbs.middleware.server.common.storage.event.jdbc.JdbcEventMapper;
import com.pbs.middleware.server.common.storage.event.jdbc.JdbcEventRepository;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadEvent;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@JdbcEnabled
@Component
public class UploadJdbcEventRepository extends JdbcEventRepository<UploadEvent, UUID> {

    private static final String UPLOAD_TABLE_NAME = "upload";

    public UploadJdbcEventRepository(
            @NonNull NamedParameterJdbcTemplate jdbcTemplate,
            @NonNull JdbcDomainIdMapper<UUID> domainIdMapper,
            @NonNull JdbcEventMapper<UploadEvent> eventMapper) {
        super(jdbcTemplate, domainIdMapper, eventMapper, UPLOAD_TABLE_NAME);
    }
}
