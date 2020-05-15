package com.pbs.middleware.server.features.pbs.qstat;

import com.pbs.middleware.server.features.pbs.PbsCommand;
import lombok.Data;

@Data
public class QstatCommand implements PbsCommand {

    private static final String QSTAT_NAME = "qstat";

    private final String format = "json";

    private String jobId;

    public QstatCommand(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return ". /etc/profile && "
                + QSTAT_NAME
                + " -x -f -F json "
                + jobId;
    }

}
