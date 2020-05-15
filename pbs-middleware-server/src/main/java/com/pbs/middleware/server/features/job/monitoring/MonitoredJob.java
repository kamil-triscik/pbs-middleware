package com.pbs.middleware.server.features.job.monitoring;

import java.util.Queue;
import java.util.UUID;
import lombok.Data;

@Data
public class MonitoredJob {

    private final UUID domainId;

    private final String jobId;

    private final Queue<MonitoredJob> queue;

    private Boolean pause = false;
}
