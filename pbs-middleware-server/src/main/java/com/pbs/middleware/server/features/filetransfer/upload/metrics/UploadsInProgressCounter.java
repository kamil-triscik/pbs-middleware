package com.pbs.middleware.server.features.filetransfer.upload.metrics;

import com.pbs.middleware.server.common.metrics.CachedSupplier;
import com.pbs.middleware.server.features.filetransfer.upload.status.repository.UploadStatusRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UploadsInProgressCounter {

    @Value("${middleware.server.metrics.upload.progress.cache-limit:15}")
    private Integer cacheLimit;

    private final Gauge gauge;

    public UploadsInProgressCounter(@NonNull MeterRegistry registry, UploadStatusRepository repository) {
        this.gauge = Gauge
                .builder("uploads_inprogress_count", new CachedSupplier<>(repository::count, cacheLimit))
                .description("Current count of uploads in progress!")
                .tags("feature", "upload")
                .register(registry);
    }

}
