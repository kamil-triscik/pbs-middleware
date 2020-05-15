package com.pbs.middleware.server.features.pbs.qdel;

import com.pbs.middleware.server.features.pbs.PbsCommand;
import lombok.Data;

import static java.lang.String.format;

@Data
public class QdelCommand implements PbsCommand {

    private static final String QDEL_NAME = "qdel";

    private String jobId;

    public QdelCommand(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return format(". /etc/profile && %s %s", QDEL_NAME,jobId);
    }

}
