package com.pbs.middleware.server.common.storage.event;

import com.pbs.middleware.server.common.domain.MiddlewareEvent;
import com.pbs.middleware.server.common.storage.EventRepository;
import java.util.Set;

public interface EventStorage<ID, EVENT extends MiddlewareEvent<ID>> extends EventRepository<EVENT, ID> {

    Set<ID> streams();

    EventStream<EVENT> stream(ID domainId);

    void copy(ID domainId, ID newDomainId);

    void delete(ID domainId);

}
