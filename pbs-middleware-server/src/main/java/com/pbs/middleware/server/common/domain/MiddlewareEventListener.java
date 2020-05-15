package com.pbs.middleware.server.common.domain;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import java.util.EventListener;

@FunctionalInterface
public interface MiddlewareEventListener<EVENT extends MiddlewareEvent> extends EventListener {

    void onEvent(EVENT event) throws MiddlewareException;

}
