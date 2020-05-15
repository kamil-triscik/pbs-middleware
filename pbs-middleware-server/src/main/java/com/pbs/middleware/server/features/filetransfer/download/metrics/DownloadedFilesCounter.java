package com.pbs.middleware.server.features.filetransfer.download.metrics;

import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloaded;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DownloadedFilesCounter {

    private final Counter counter;

    public DownloadedFilesCounter(@NonNull MeterRegistry registry) {
        this.counter = Counter
                .builder("downloaded_files_count")
                .description("Count how many files were downloaded!")
                .tags("feature", "download")
                .register(registry);
    }

    @EventListener
    public void onApplicationEvent(final FileDownloaded event) {
        counter.increment();
    }


}
