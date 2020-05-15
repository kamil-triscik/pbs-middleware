package com.pbs.middleware.server.features.filetransfer.download.metrics;

import com.pbs.middleware.server.features.filetransfer.download.events.DownloadInitialized;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NewDownloadsCounter {

    private final Counter counter;

    public NewDownloadsCounter(@NonNull MeterRegistry registry) {
        this.counter = Counter
                .builder("downloads_total_count")
                .description("Count how many downloads were started!")
                .tags("feature", "download")
                .register(registry);
    }

    @EventListener
    public void onApplicationEvent(final DownloadInitialized event) {
        counter.increment();
    }


}
