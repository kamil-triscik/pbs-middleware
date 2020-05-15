package com.pbs.middleware.server.common.domain;

public interface Visitor<EVENT extends MiddlewareEvent> {

    void visit(EVENT event);

}
