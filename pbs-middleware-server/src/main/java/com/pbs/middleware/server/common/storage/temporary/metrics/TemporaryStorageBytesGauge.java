package com.pbs.middleware.server.common.storage.temporary.metrics;

import com.pbs.middleware.server.common.metrics.CachedSupplier;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TemporaryStorageBytesGauge {

    private final Gauge gauge;

    public TemporaryStorageBytesGauge(@NonNull MeterRegistry registry, TemporaryStorage temporaryStorage) {
        CachedSupplier<Number> totalSizeSupplier = new CachedSupplier<>(temporaryStorage::getTotalSize);
        this.gauge = Gauge
                .builder("temporary_storage_size", totalSizeSupplier)
                .description("Temporary storage size in bytes!")
                .tags("feature", "temporary_storage")
                .register(registry);
    }

}
