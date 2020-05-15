package com.pbs.middleware.server.common.domain;

/**
 * The notifier allows other classes to notify the receiver about {@link MiddlewareEvent}.
 */
public interface MiddlewareEventNotifier<T extends MiddlewareEvent> {

    /**
     * Notifies the receiver about a {@link MiddlewareEvent} happened.
     */
    default void publish(T event) {}

}
