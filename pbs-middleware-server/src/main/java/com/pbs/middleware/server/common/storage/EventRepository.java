package com.pbs.middleware.server.common.storage;

import com.pbs.middleware.server.common.domain.Id;
import com.pbs.middleware.server.common.domain.MiddlewareEvent;
import java.util.List;
import java.util.Set;

public interface EventRepository<EVENT extends MiddlewareEvent<ID>, ID> {

    List<EVENT> findAll();

    Set<Id<ID>> findAllIds();

    List<EVENT> findAllByDomainId(ID domainId);

    List<EVENT> findAllByDomainId(ID domainId, Integer limit);

    void deleteAllByDomainId(ID domainId);

    EVENT save(EVENT event);



}
