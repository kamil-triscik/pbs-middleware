package com.pbs.middleware.server.features.filetransfer.upload.metrics;

import com.pbs.middleware.server.features.filetransfer.upload.events.FileProvided;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploaded;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UploadedBytesCounter {

    private final Counter counter;

    public UploadedBytesCounter(@NonNull MeterRegistry registry) {
        this.counter = Counter
                .builder("uploaded_bytes_count")
                .description("Count how many bytes were uploaded!")
                .tags("feature", "upload")
                .register(registry);
    }

    @EventListener
    public void onApplicationEvent(final FileUploaded event) {
        counter.increment(event.getSize());
    }

}
