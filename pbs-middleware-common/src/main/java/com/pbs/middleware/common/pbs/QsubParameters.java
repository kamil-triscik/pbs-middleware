package com.pbs.middleware.common.pbs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QsubParameters {

    private Resources resources;

    private Map<String, String> variables;

    private String stdout;

    private String stderr;

    private String prefix;

    private String script;

    private String suffix;

    private String queue;

    private String jobName;

    private String workdir;

    private List<String> arguments = new LinkedList<>();

}
