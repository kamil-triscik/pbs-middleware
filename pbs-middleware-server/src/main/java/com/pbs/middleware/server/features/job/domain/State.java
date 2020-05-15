package com.pbs.middleware.server.features.job.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum State {

    UNKNOWN(""),//default for object,
    NOT_STARTED("n"),
    INITIALIZING("I"),
    QUEUED("Q"),
    RUNNING("R"),
    HELD("H"),
    MOVED("M"),
    FINISHED("F"),
    EXITING("E");

    @Getter
    private final String pbsLabel;

    public static State of(String state) {
        switch (state.toUpperCase()) {
            case "I":
                return INITIALIZING;
            case "Q":
                return QUEUED;
            case "R":
                return RUNNING;
            case "H":
                return HELD;
            case "M":
                return MOVED;
            case "F":
                return FINISHED;
            case "E":
                return EXITING;
            default:
                return UNKNOWN;
        }
    }

}
