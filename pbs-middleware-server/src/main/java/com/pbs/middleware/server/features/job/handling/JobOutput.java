package com.pbs.middleware.server.features.job.handling;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JobOutput {

    @NonNull
    private final String std;

    @NonNull
    private final String err;

}
