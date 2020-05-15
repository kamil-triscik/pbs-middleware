package com.pbs.middleware.server.features.pbs.qsub;

import lombok.Getter;

public enum ScratchType {
    SSD("ssd"),
    SHARED("shared"),
    LOCAL("local");

    @Getter
    private String label;

    ScratchType(String label) {
        this.label = label;
    }
}
