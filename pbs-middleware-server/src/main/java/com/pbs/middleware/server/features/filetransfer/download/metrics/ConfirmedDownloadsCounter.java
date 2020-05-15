package com.pbs.middleware.server.features.filetransfer.download.metrics;

import com.pbs.middleware.server.features.filetransfer.download.events.DownloadConfirmed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ConfirmedDownloadsCounter {

    private final Counter counter;

    public ConfirmedDownloadsCounter(@NonNull MeterRegistry registry) {
        this.counter = Counter
                .builder("confirmed_downloads_count")
                .description("Count how many downloads were confirmed!")
                .tags("feature", "download")
                .register(registry);
    }

    @EventListener
    public void onApplicationEvent(final DownloadConfirmed event) {
        counter.increment();
    }


}
