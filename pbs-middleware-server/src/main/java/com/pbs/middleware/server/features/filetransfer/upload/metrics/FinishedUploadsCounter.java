package com.pbs.middleware.server.features.filetransfer.upload.metrics;

import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFinished;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FinishedUploadsCounter {

    private final Counter counter;

    public FinishedUploadsCounter(@NonNull MeterRegistry registry) {
        this.counter = Counter
                .builder("finished_uploads_count")
                .description("Count how many uploads were finished!")
                .tags("feature", "upload")
                .register(registry);
    }

    @EventListener
    public void onApplicationEvent(final UploadFinished event) {
        counter.increment();
    }


}
