package com.pbs.middleware.server.features.filetransfer.download.metrics;

import com.pbs.middleware.server.features.filetransfer.download.events.DownloadFailed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FailedDownloadsCounter {

    private final Counter counter;

    public FailedDownloadsCounter(@NonNull MeterRegistry registry) {
        this.counter = Counter
                .builder("failed_downloads_count")
                .description("Count how many downloads failed!")
                .tags("feature", "download")
                .register(registry);
    }

    @EventListener
    public void onApplicationEvent(final DownloadFailed event) {
        counter.increment();
    }


}
