package com.pbs.middleware.server.features.job.utils.merge;

import com.pbs.middleware.server.common.utils.AbstractMerger;
import com.pbs.middleware.common.pbs.QsubParameters;
import java.util.ArrayList;
import java.util.List;

import static com.pbs.middleware.server.features.job.utils.merge.ResourcesMerger.mergeResources;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class QsubParamsMerger extends AbstractMerger<QsubParameters> {

    public static QsubParameters mergeQsubParams(QsubParameters origin, QsubParameters changes) {
        if (changes == null) {
            return origin;
        }

        QsubParameters merged = deepCopy(origin);

        merged.setResources(mergeResources(origin.getResources(), changes.getResources()));

        if (isSet(changes.getVariables())) {
            merged.setVariables(mergeMap(merged.getVariables(), changes.getVariables()));
        }

        if (isSet(changes.getStdout())) {
            merged.setStdout(changes.getStdout());
        }

        if (isSet(changes.getStderr())) {
            merged.setStderr(changes.getStderr());
        }

        if (isSet(changes.getPrefix())) {
            merged.setPrefix(changes.getPrefix());
        }

        if (isSet(changes.getScript())) {
            merged.setScript(changes.getScript());
        }

        if (isSet(changes.getSuffix())) {
            merged.setSuffix(changes.getSuffix());
        }

        if (isSet(changes.getQueue())) {
            merged.setQueue(changes.getQueue());
        }

        if (isSet(changes.getJobName())) {
            merged.setJobName(changes.getJobName());
        }

        if (isSet(changes.getWorkdir())) {
            merged.setWorkdir(changes.getWorkdir());
        }

        if (isSet(changes.getArguments())) {
            List<String> mergedArgs = new ArrayList<>();
            if (merged.getArguments() != null) {
                mergedArgs.addAll(merged.getArguments());
            }
            mergedArgs.addAll(changes.getArguments());
            mergedArgs.removeIf(arg -> arg == null || isBlank(arg));
            merged.setArguments(mergedArgs);
        }

        return merged;

    }

}
