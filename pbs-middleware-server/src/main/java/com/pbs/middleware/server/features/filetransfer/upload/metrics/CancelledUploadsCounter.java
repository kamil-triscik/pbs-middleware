package com.pbs.middleware.server.features.filetransfer.upload.metrics;

import com.pbs.middleware.server.features.filetransfer.upload.events.UploadCancelled;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CancelledUploadsCounter {

    private final Counter counter;

    public CancelledUploadsCounter(@NonNull MeterRegistry registry) {
        this.counter = Counter
                .builder("cancelled_uploads_count")
                .description("Count how many uploads were cancelled!")
                .tags("feature", "upload")
                .register(registry);
    }

    @EventListener
    public void onApplicationEvent(final UploadCancelled event) {
        counter.increment();
    }

}
