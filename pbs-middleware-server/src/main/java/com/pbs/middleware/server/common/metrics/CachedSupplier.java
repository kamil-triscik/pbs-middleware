package com.pbs.middleware.server.common.metrics;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * This cache supplier is used for metrics to avoid calling supplier too often
 * what can cause high load for example on database.
 *
 * {@link java.util.function.Supplier#get()} will be called only in case, {@link #value} is null
 * or {@link #lastCheck} is older then {@link #cacheLimit}.
 *
 * {@link #cacheLimit} is value in seconds, how old value we can provide instead calling {@link java.util.function.Supplier#get()}.
 *
 * @param <T> the type of results supplied by this CachedSupplier
 */
public class CachedSupplier<T> implements Supplier<T> {

    private Integer cacheLimit = 60;

    private T value = null;

    private Date lastCheck;

    private final Supplier<T> supplier;

    public CachedSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public CachedSupplier(Supplier<T> supplier, Integer cacheLimit) {
        this.supplier = supplier;
        this.cacheLimit = cacheLimit;
    }

    @Override
    public T get() {
        if (value == null) {
            this.value = supplier.get();
            lastCheck = new Date();
        } else if(secondsSinceLastCheck() > cacheLimit) {
                this.value = supplier.get();
                lastCheck = new Date();
        }
        return this.value;
    }

    private Long secondsSinceLastCheck() {
        return TimeUnit.SECONDS.convert(Math.abs(new Date().getTime() - lastCheck.getTime()), TimeUnit.MILLISECONDS);
    }

}
