package com.pbs.middleware.server.features.job.handling.handler;

import com.pbs.middleware.server.features.job.domain.Job;
import com.pbs.middleware.server.features.job.handling.JobOutput;
import com.pbs.middleware.server.features.job.handling.JobServiceProvider;
import com.pbs.middleware.server.features.job.handling.ScriptExecutionException;
import com.pbs.middleware.server.features.job.handling.containers.JobConfigurationContainer;
import com.pbs.middleware.server.features.job.handling.containers.JobContainer;
import com.pbs.middleware.server.features.job.listeners.JobPublisher;
import com.pbs.middleware.server.features.pbs.qstat.Qstat;
import com.pbs.middleware.server.features.script.repository.Script;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomScriptHandler implements ScriptHandler {

    private final Job job;

    private final JobPublisher jobPublisher;

    private final JobOutput output;

    private final Qstat qstat;

    private final Script script;

    @Override
    public void execute() {
        try {
            Binding binding = new Binding();

            getBindingData().forEach(binding::setVariable);
            getBindingServices().forEach(binding::setVariable);

            new GroovyShell(binding).evaluate(script.getCode());
        } catch (Exception e) {
            throw new ScriptExecutionException(script.getId(), e);
        }
    }

    private Map<String, Object> getBindingData() {
        Map<String, Object> bindingData = new HashMap<>();

        JobContainer job = new JobContainer(
                this.job.getId(),
                this.job.getPbsJobId(),
                this.job.getRestarts(),
                qstat,
                output.getStd(),
                output.getErr(),
                new JobConfigurationContainer(this.job.getConfiguration())
        );

        bindingData.put("job", job);

        return bindingData;
    }

    private Map<String, Object> getBindingServices() {
        Map<String, Object> bindingServices = new HashMap<>();

        bindingServices.put("service", new JobServiceProvider(job.getId(), job.getOwner(), jobPublisher));

        return bindingServices;
    }
}
