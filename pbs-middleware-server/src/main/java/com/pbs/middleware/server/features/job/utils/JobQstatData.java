package com.pbs.middleware.server.features.job.utils;

import com.pbs.middleware.server.features.job.domain.State;
import lombok.Data;

@Data
public class JobQstatData {

    private State state = State.UNKNOWN;

    private Integer subState;

    private String comment;

    private String execHost;

    private String execVNode;

    private Integer runCount = 0;

}
