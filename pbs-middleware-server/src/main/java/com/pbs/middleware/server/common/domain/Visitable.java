package com.pbs.middleware.server.common.domain;

public interface Visitable<EVENT extends MiddlewareEvent> {

    void accept(Visitor<EVENT> visitor);

}
