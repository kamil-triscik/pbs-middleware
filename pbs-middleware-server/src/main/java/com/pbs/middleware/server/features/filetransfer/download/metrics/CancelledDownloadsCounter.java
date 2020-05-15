package com.pbs.middleware.server.features.filetransfer.download.metrics;

import com.pbs.middleware.server.features.filetransfer.download.events.DownloadCancelled;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CancelledDownloadsCounter {

    private final Counter counter;

    public CancelledDownloadsCounter(@NonNull MeterRegistry registry) {
        this.counter = Counter
                .builder("cancelled_downloads_count")
                .description("Count how many downloads were cancelled!")
                .tags("feature", "download")
                .register(registry);
    }

    @EventListener
    public void onApplicationEvent(final DownloadCancelled event) {
        counter.increment();
    }

}
