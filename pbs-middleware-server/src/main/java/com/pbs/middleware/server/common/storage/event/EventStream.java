package com.pbs.middleware.server.common.storage.event;

import com.pbs.middleware.server.common.domain.MiddlewareEvent;
import com.pbs.middleware.server.common.exception.MiddlewareException;
import java.util.List;

public interface EventStream<EVENT extends MiddlewareEvent> {

    void store(EVENT event) throws MiddlewareException;

    default List<EVENT> fetch() {
        return fetch(Integer.MAX_VALUE);
    }

    List<EVENT> fetch(Integer version);

    default Integer lastVersion() {
        return fetch().size();
    }

}
