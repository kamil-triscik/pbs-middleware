package com.pbs.middleware.server.features.job.handling.containers;

import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import com.pbs.middleware.common.pbs.QsubParameters;
import com.pbs.middleware.common.pbs.Resources;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public final class JobConfigurationContainer {

    public JobConfigurationContainer(JobConfiguration jobConfiguration) {
        this.templateId = jobConfiguration.getTemplateId();
        this.resources = jobConfiguration.getQsubParameters().getResources();
        this.queue = jobConfiguration.getQsubParameters().getQueue();
        this.variables = jobConfiguration.getQsubParameters().getVariables();
        this.workdir = jobConfiguration.getQsubParameters().getWorkdir();
        this.qsubPrefix = jobConfiguration.getQsubParameters().getPrefix();
        this.jobScript = jobConfiguration.getQsubParameters().getScript();
        this.qsubSuffix = jobConfiguration.getQsubParameters().getSuffix();
        this.stdout = jobConfiguration.getQsubParameters().getStdout();
        this.stderr = jobConfiguration.getQsubParameters().getStderr();
        this.arguments = jobConfiguration.getQsubParameters().getArguments();
        this.group = jobConfiguration.getGroup();
        this.jobName = jobConfiguration.getQsubParameters().getJobName();
        this.connection = jobConfiguration.getConnection();
        this.handleStatusScript = jobConfiguration.getScript();
    }

    private String templateId;

    private Resources resources;

    private String queue;

    private Map<String, String> variables;

    private String workdir;

    private String qsubPrefix;

    private String jobScript;

    private String qsubSuffix;

    private String stdout;

    private String stderr;

    private List<String> arguments;

    private String group;

    private String jobName;

    private String connection;

    private String handleStatusScript;

    public JobConfigurationContainer addMemory(String mem) {

        return this;
    }



    public JobConfigurationContainer addMemory(Integer mem) {

        return this;
    }

    public JobConfigurationContainer setMemory(String mem) {

        return this;
    }

    public JobConfigurationContainer walltime(String walltime) {

        return this;
    }

    public JobConfiguration getTemplateProperties() {
        JobConfiguration template = new JobConfiguration();

        template.setGroup(group);
        template.setConnection(connection);
        template.setScript(handleStatusScript);

        QsubParameters parameters = new QsubParameters();

        parameters.setResources(resources);
        parameters.setVariables(variables);
        parameters.setStdout(stdout);
        parameters.setStderr(stderr);
        parameters.setPrefix(qsubPrefix);
        parameters.setScript(jobScript);
        parameters.setSuffix(qsubSuffix);
        parameters.setQueue(queue);
        parameters.setJobName(jobName);
        parameters.setWorkdir(workdir);
        parameters.setArguments(arguments);
        template.setQsubParameters(parameters);

        return template;
    }

}
