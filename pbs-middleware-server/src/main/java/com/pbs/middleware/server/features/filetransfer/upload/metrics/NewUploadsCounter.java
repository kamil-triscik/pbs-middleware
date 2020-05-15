package com.pbs.middleware.server.features.filetransfer.upload.metrics;

import com.pbs.middleware.server.features.filetransfer.upload.events.UploadInitialized;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NewUploadsCounter {

    private final Counter counter;

    public NewUploadsCounter(@NonNull MeterRegistry registry) {
        this.counter = Counter
                .builder("uploads_total_count")
                .description("Count how many uploads were started!")
                .tags("feature", "upload")
                .register(registry);
    }

    @EventListener
    public void onApplicationEvent(final UploadInitialized event) {
        counter.increment();
    }


}
