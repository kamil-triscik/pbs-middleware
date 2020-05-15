package com.pbs.middleware.server.features.filetransfer.upload.metrics;

import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploaded;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UploadedFilesCounter {

    private final Counter counter;

    public UploadedFilesCounter(@NonNull MeterRegistry registry) {
        this.counter = Counter
                .builder("uploaded_files_count")
                .description("Count how many files were uploaded!")
                .tags("feature", "upload")
                .register(registry);
    }

    @EventListener
    public void onApplicationEvent(final FileUploaded event) {
        counter.increment();
    }


}
