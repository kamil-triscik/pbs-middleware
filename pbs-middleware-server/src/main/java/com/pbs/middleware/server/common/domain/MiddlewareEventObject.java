package com.pbs.middleware.server.common.domain;

/**
 * The [DomainObject] is a superclass of all domain objects. It also represents an aggregate root in terms
 * of _Domain Driven Design_.
 * <p>
 * The [DomainObject] also follows _Event Sourcing_ approach where all requested modifications to [DomainObject]'s
 * state are represented as [DomainEvent]s. These [DomainEvent]s are replayed on the [DomainObject] in chronological
 * order as they happened in the past.
 * <p>
 * Finally, the [DomainObject] follows _Command Query Separation_ approach by clearly defining _command_ methods and
 * _query_ methods. The command methods return nothing, and also do not directly change state of the [DomainObject].
 * Instead, the command methods notify the [DomainObject] that an [DomainEvent] just happened and the [DomainObject]
 * is required to apply a modification its internal state based on information provided in the [DomainEvent].
 */
public abstract class MiddlewareEventObject<EVENT extends MiddlewareEvent> {

    /**
     * Applies an [e]vent to this [DomainObject]. The [DomainObject] is required to update its internal state.
     * <p>
     * The [e]vent represents some change in this [DomainObject] which happened at some time in the past. All passed
     * [DomainEvent]s are usually applied to [DomainObject]s by factories.
     */
    public abstract void apply(EVENT event);


    private MiddlewareEventObject<EVENT> getDomainObject() {
        return this;
    }


}
