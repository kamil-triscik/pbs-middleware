package com.pbs.middleware.server.common.domain;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * The [DomainEvent] represents a modification to [DomainObject]'s internal state when a command method was executed
 * on that [DomainObject].
 *
 * To maintain consistency, traceability and history of a [DomainObject], [DomainEvent] must be immutable. And they
 * must be applied to a [DomainObject] in the same chronological order in which they actually happened.
 *
 * All event-driven logic must be performed only during event notification, which is usually the time when
 * the [DomainEvent] actually happened. When the [DomainEvent] is replayed or applied to a [DomainObject], then no
 * event-driven logic must be triggered.
 */

@Getter
public abstract class MiddlewareEvent<T> {

    @Setter
    private UUID eventId = UUID.randomUUID();

    @Setter
    @Getter
    @Indexed
    private T domainId;

    @Setter
    private Instant instant = Instant.now();

    private String eventType = MiddlewareEvent.class.getName();

    public MiddlewareEvent(T domainId) {
        this.domainId = domainId;
    }

}
