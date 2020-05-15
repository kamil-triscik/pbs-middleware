package com.pbs.middleware.server.features.template.repository;


import com.pbs.middleware.server.common.domain.Id;
import com.pbs.middleware.server.common.storage.event.mongo.MongoEnabled;
import com.pbs.middleware.server.common.storage.EventRepository;
import com.pbs.middleware.server.features.template.events.TemplateEvent;
import java.util.List;
import java.util.Set;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

@MongoEnabled
public interface TemplateMongoEventRepository extends MongoRepository<TemplateEvent, String>, EventRepository<TemplateEvent, String> {

    List<TemplateEvent> findAllByDomainId(String id);

    void deleteAllByDomainId(String id);

    @Query(value = "{}", fields = "{domainId : 1, _id : 0}")
    Set<Id<String>> findAllIds();

}
