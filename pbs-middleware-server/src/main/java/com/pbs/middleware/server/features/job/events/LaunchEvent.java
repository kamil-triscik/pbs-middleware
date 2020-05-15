package com.pbs.middleware.server.features.job.events;

import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import java.util.UUID;

public interface LaunchEvent {

    UUID getOwner();

    JobConfiguration getJobConfiguration();

    UUID getDomainId();
}
