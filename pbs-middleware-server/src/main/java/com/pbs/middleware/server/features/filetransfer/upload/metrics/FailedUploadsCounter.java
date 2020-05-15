package com.pbs.middleware.server.features.filetransfer.upload.metrics;

import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFailed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FailedUploadsCounter {

    private final Counter counter;

    public FailedUploadsCounter(@NonNull MeterRegistry registry) {
        this.counter = Counter
                .builder("failed_uploads_count")
                .description("Count how many uploads failed!")
                .tags("feature", "upload")
                .register(registry);
    }

    @EventListener
    public void onApplicationEvent(final UploadFailed event) {
        counter.increment();
    }


}
