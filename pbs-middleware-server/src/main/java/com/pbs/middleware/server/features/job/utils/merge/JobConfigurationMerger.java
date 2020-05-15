package com.pbs.middleware.server.features.job.utils.merge;

import com.pbs.middleware.server.common.utils.AbstractMerger;
import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("PMD.UselessParentheses")
public class JobConfigurationMerger extends AbstractMerger<JobConfiguration> {

    public static JobConfiguration merge(JobConfiguration origin, JobConfiguration changes) {
        if (changes == null) {
            return origin;
        }
        JobConfiguration merged = deepCopy(origin);

        if (isSet(changes.getGroup())) {
            merged.setGroup(changes.getGroup());
        }

        if (isSet(changes.getConnection())) {
            merged.setConnection(changes.getConnection());
        }

        if (isSet(changes.getScript())) {
            merged.setScript(changes.getScript());
        }

        merged.setQsubParameters(QsubParamsMerger.mergeQsubParams(origin.getQsubParameters(), changes.getQsubParameters()));

        return merged;
    }


}
