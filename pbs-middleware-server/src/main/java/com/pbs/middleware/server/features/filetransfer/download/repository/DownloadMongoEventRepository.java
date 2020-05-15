package com.pbs.middleware.server.features.filetransfer.download.repository;

import com.pbs.middleware.server.common.domain.Id;
import com.pbs.middleware.server.common.storage.event.mongo.MongoEnabled;
import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadEvent;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

@MongoEnabled
public interface DownloadMongoEventRepository extends MongoRepository<DownloadEvent, UUID>, EventRepository<DownloadEvent, UUID> {

    List<DownloadEvent> findAllByDomainId(UUID id);

    @Query(value = "{}", fields = "{domainId : 1, _id : 0}")
    Set<Id<UUID>> findAllIds();

    void deleteAllByDomainId(UUID domainID);

}
