package com.pbs.middleware.server.features.pbs.qsub;

import com.pbs.middleware.server.features.pbs.PbsCommand;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Builder
@Data
@RequiredArgsConstructor
public class QsubCommand implements PbsCommand {

    public static final String QSUB_NAME = "qsub";
    public static final String QSUB_PATH = QSUB_NAME;

    private final String prefix;

    private final String suffix;

    private final String workDirectory;

    private final String name;

    private final String vars;

    private final String queue;

    private final String resources;

    private final String outputPath;

    private final String errorPath;

    private final String arguments;

    private final String script;

    //  . /etc/profile && qsub -l walltime=10:0 -l select=1:ncpus=1:mem=400mb -N testovaci_uloha -v PROMENNA=\"deset\" run.sh";
    @Override
    public String toString() {
        return prefix
                + workDirectory
                + QSUB_PATH
                + name
                + (isBlank(vars) ? "" : " -v " + vars)
                + queue
                + (isBlank(resources) ? "" : resources)
                + outputPath
                + errorPath
                + arguments
                + script
                + suffix;
    }
}
