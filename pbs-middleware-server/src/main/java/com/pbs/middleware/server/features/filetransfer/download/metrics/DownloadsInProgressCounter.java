package com.pbs.middleware.server.features.filetransfer.download.metrics;

import com.pbs.middleware.server.common.metrics.CachedSupplier;
import com.pbs.middleware.server.features.filetransfer.download.status.repository.DownloadStatusRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class DownloadsInProgressCounter {

    private final Gauge gauge;

    public DownloadsInProgressCounter(@NonNull MeterRegistry registry, DownloadStatusRepository repository) {
        this.gauge = Gauge
                .builder("downloads_inprogress_count", new CachedSupplier<>(repository::count))
                .description("Current count of downloads in progress!")
                .tags("feature", "download")
                .register(registry);
    }

}
