package com.pbs.middleware.server.features.job.domain;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Job state getter test")
class JobStateInfoTest {

    @ParameterizedTest
    @CsvSource({
            "UNKNOWN,RUNNING",
            "NOT_STARTED,RUNNING",
            "INITIALIZING,RUNNING",
            "QUEUED,RUNNING",
            "RUNNING,RUNNING",
            "HELD,RUNNING",
            "MOVED,RUNNING",
            "EXITING,RUNNING",
            "FINISHED,FINISHED"
    })
    void getStatus(State actual, State expected) {
        assertEquals(expected, new JobState(UUID.randomUUID(), actual).getStatus());

    }
}