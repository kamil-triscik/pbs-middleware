package com.pbs.middleware.server.common.storage.temporary.metrics;

import com.pbs.middleware.server.common.metrics.CachedSupplier;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TemporaryStorageFilesGauge {

    private final Gauge gauge;

    public TemporaryStorageFilesGauge(@NonNull MeterRegistry registry, TemporaryStorage temporaryStorage) {
        CachedSupplier<Number> filesCountSupplier = new CachedSupplier<>(temporaryStorage::getCount);
        this.gauge = Gauge
                .builder("temporary_storage_files_count", filesCountSupplier)
                .description("Temporary storage files count!")
                .tags("feature", "temporary_storage")
                .register(registry);
    }

}
