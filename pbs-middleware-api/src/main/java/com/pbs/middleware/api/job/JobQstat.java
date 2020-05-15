package com.pbs.middleware.api.job;

import com.pbs.middleware.common.pbs.Walltime;
import io.swagger.annotations.ApiModel;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
@ApiModel(value = "JobQstat", description = "DTO representing job qstat.")
public class JobQstat {

    //todo
    private Long timestamp;

    private String pbsServer;

    private String jobId;

    private String jobName;

    private String jobOwner;

    private State jobState;

    private String queue;

    private String jobDir;

    private String errorPath;

    private String outputPath;

    private String comment;

    private Integer substate;

    private Map<String, Object> resourcesUsed;

    private Map<String, Object> resourcesList;

    private Map<String, Object> variableList;

    private List<Walltime> walltimes;

    private List<String> cput;

}
