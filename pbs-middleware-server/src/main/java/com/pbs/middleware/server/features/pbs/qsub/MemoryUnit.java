package com.pbs.middleware.server.features.pbs.qsub;

import lombok.Getter;

public enum MemoryUnit {
    MB("mb"),
    GB("gb");

    @Getter
    private String label;

    MemoryUnit(String label) {
        this.label = label;
    }
}
