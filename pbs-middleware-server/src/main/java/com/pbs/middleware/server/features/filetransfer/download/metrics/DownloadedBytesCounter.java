package com.pbs.middleware.server.features.filetransfer.download.metrics;

import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloaded;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DownloadedBytesCounter {

    private final Counter counter;

    public DownloadedBytesCounter(@NonNull MeterRegistry registry) {
        this.counter = Counter
                .builder("downloaded_bytes_count")
                .description("Count how many bytes were downloaded!")
                .tags("feature", "download")
                .register(registry);
    }

    @EventListener
    public void onApplicationEvent(final FileDownloaded event) {
        counter.increment(event.getSize());
    }

}
