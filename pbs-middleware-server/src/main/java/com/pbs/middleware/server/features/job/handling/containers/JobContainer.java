package com.pbs.middleware.server.features.job.handling.containers;

import com.pbs.middleware.server.features.pbs.qstat.Qstat;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JobContainer {

    @NonNull
    private final UUID id;

    @NonNull
    private final String pbsId;

    @NonNull
    private final Integer restarts;

    @NonNull
    private final Qstat qstat;

    @NonNull
    private final String stdout;

    @NonNull
    private final String stderr;

    @NonNull
    private final JobConfigurationContainer config;
}
