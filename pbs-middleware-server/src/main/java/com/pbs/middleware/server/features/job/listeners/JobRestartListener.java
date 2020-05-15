package com.pbs.middleware.server.features.job.listeners;

import com.pbs.middleware.server.common.exception.MiddlewareException;
import com.pbs.middleware.server.features.job.events.JobRestartRequestEvent;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class JobRestartListener {

    @EventListener
    public void onApplicationEvent(final JobRestartRequestEvent event) throws MiddlewareException {
        waitForRestart(event.getDuration());
    }

    private void waitForRestart(Duration duration) {
        if (duration != null) {
            log.info("Waiting {} before restart", duration.toString());
            try {
                Thread.sleep(duration.toMillis());
            } catch (InterruptedException e) {
                log.warn("Job restart postpone failed", e);
            }
        }
    }
}
