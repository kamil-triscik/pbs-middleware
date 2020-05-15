package com.pbs.middleware.server.features.filetransfer.download.repository;


import com.pbs.middleware.server.common.storage.event.jdbc.JdbcDomainIdMapper;
import com.pbs.middleware.server.common.storage.event.jdbc.JdbcEnabled;
import com.pbs.middleware.server.common.storage.event.jdbc.JdbcEventMapper;
import com.pbs.middleware.server.common.storage.event.jdbc.JdbcEventRepository;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadEvent;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@JdbcEnabled
@Component
public class DownloadJdbcEventRepository extends JdbcEventRepository<DownloadEvent, UUID> {

    private static final String DOWNLOAD_TABLE_NAME = "download";

    public DownloadJdbcEventRepository(
            @NonNull NamedParameterJdbcTemplate jdbcTemplate,
            @NonNull JdbcDomainIdMapper<UUID> domainIdMapper,
            @NonNull JdbcEventMapper<DownloadEvent> eventMapper) {
        super(jdbcTemplate, domainIdMapper, eventMapper, DOWNLOAD_TABLE_NAME);
    }
}
