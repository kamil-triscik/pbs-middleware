package com.pbs.middleware.api.job;

import com.pbs.middleware.common.pbs.Resources;
import io.swagger.annotations.ApiModel;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

//@ConnectionMandatory
//@ScriptMandatory
@ApiModel(value = "JobRestartRequest", description = "Job DTO for restarting job reuqest")
public final class JobRestartRequest extends JobSubmit {

    JobRestartRequest(String templateId, @Valid Resources resources, String queue, Map<String, String> variables, String workdir, String qsubPrefix, String jobScript, String qsubSuffix, String stdout, String stderr, List<String> arguments, String group, String jobName, String connection, String handleStatusScript) {
        super(templateId, resources, queue, variables, workdir, qsubPrefix, jobScript, qsubSuffix, stdout, stderr, arguments, group, jobName, connection, handleStatusScript);
    }
}
