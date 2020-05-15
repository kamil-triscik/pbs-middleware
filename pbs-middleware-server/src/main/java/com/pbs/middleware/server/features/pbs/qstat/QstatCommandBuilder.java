package com.pbs.middleware.server.features.pbs.qstat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QstatCommandBuilder {

    public QstatCommand build(String jobId) {
        return new QstatCommand(jobId);
    }

    public QstatCommand build(List<String> jobIds) {
        return build(new HashSet<>(jobIds));
    }

    public QstatCommand build(Set<String> jobIds) {
        return new QstatCommand(String.join(" ", jobIds));
    }
}
