package com.pbs.middleware.server.features.pbs.qstat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pbs.middleware.server.features.job.domain.State;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
@JsonDeserialize(using= QstatDeserializer.class)
public class Qstat {

    private Long timestamp;

    @JsonAlias("pbs_server")
    private String pbsServer;

    private String jobId;

    private Integer exitStatus;

    @JsonAlias("Job_Name")
    private String jobName;

    @JsonAlias("Job_Owner")
    private String jobOwner;

    @JsonAlias("job_state")
    private State jobState;

    private String queue;

    private String jobDir;

    @JsonAlias("Error_Path")
    private String errorPath;

    @JsonAlias("Output_Path")
    private String outputPath;

    private String comment;

    private Integer substate;

    private String execHost;

    private String execVnode;

    private Integer runCount;

    @JsonAlias("resources_used")
    private Map<String, Object> resourcesUsed;

    @JsonAlias("Resource_List")
    private Map<String, Object> resourcesList;

    @JsonAlias("Variable_List")
    private Map<String, Object> variableList;

    private List<String> walltimes;

    private List<String> cput;

    public static Qstat unknown() {
        return unknown("QStat command probably not launched yet!");
    }

    public static Qstat unknown(String message) {
        Qstat unknown = new Qstat();
        unknown.setJobState(State.UNKNOWN);
        unknown.setComment(message);
        return unknown;
    }

}
