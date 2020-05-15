package com.pbs.middleware.server.features.pbs.qsub;

import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import com.pbs.middleware.common.pbs.Resources;
import com.pbs.middleware.server.features.template.domain.Template;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

import static java.util.Optional.ofNullable;

public class QsubCommandBuilder {

    public QsubCommand build(Template template) {
        return build(JobConfiguration.of(template));
    }

    public QsubCommand build(JobConfiguration jobConfiguration) {

        return QsubCommand.builder()
                .prefix(prefix(jobConfiguration))
                .workDirectory(workDir(jobConfiguration))
                .name(name(jobConfiguration))
                .vars(variables(jobConfiguration))
                .queue(queue(jobConfiguration))
                .resources(resources(jobConfiguration))
                .outputPath(outputPath(jobConfiguration))
                .errorPath(errorPath(jobConfiguration))
                .arguments(arguments(jobConfiguration))
                .script(script(jobConfiguration))
                .suffix(suffix(jobConfiguration))
                .build();
    }

    public String buildToString(JobConfiguration jobConfiguration) {
        return build(jobConfiguration).toString();
    }

    public String buildToString(Template template) {
        return build(template).toString();
    }


    private static String prefix(JobConfiguration templateProps) {
        return ofNullable(templateProps.getQsubParameters().getPrefix())
                .filter(StringUtils::isNotBlank)
                .map(QsubCommandBuilder::addSuffixToPrefix)
                .orElse("");
    }

    private static String addSuffixToPrefix(final String originalPrefix) {
        String prefix = originalPrefix.replaceAll("\\s+$", "");
        if (prefix.endsWith("&&")) {
            return prefix + " ";
        }
        return prefix + " && ";
    }

    private static String suffix(JobConfiguration templateProps) {
        return ofNullable(templateProps.getQsubParameters().getSuffix())
                .filter(StringUtils::isNotBlank)
                .map(QsubCommandBuilder::addPrefixToSuffix)
                .orElse("");
    }

    private static String addPrefixToSuffix(final String originalSuffix) {
        String suffix = originalSuffix.replaceFirst("^\\s*", "");
        if (suffix.startsWith("&&")) {
            return " " + suffix;
        }
        return " && " + suffix;
    }

    private static String workDir(JobConfiguration templateProps) {
        return ofNullable(templateProps.getQsubParameters().getWorkdir())
                .filter(StringUtils::isNotBlank)
                .map(dir -> "cd " + dir + ";")
                .orElse("");
    }

    private static String name(JobConfiguration templateProps) {
        return ofNullable(templateProps.getQsubParameters().getJobName())
                .filter(StringUtils::isNotBlank)
                .map(name -> " -N " + name)
                .orElse("");
    }

    private static String variables(JobConfiguration templateProps) {
        Map<String, String> vars = templateProps.getQsubParameters().getVariables();
        return ofNullable(vars)
                .map(variables -> variables.keySet().stream()
                        .filter(Objects::nonNull)
                        .filter(StringUtils::isNotBlank)
                        .filter(key -> vars.get(key) != null)
                        .filter(key -> StringUtils.isNotBlank(vars.get(key)))
                        .map(key -> key + "=\"" + vars.get(key) + "\"")
                        .collect(Collectors.joining(",")))
                .filter(StringUtils::isNotBlank)
                .map(it -> " " + it + " ")
                .orElse("");
    }

    private static String queue(JobConfiguration templateProps) {
        return ofNullable(templateProps.getQsubParameters().getQueue())
                .filter(StringUtils::isNotBlank)
                .map(queue -> " -q " + queue)
                .orElse("");
    }

    private static String resources(JobConfiguration templateProps) {
        Resources resources = templateProps.getQsubParameters().getResources();
        return resources == null ? "" : new QsubResourcesBuilder()
                .walltime(resources.getWalltime())
                .customResource(resources.getCustomResources())
                .chunks(resources.getChunks())
                .build();
    }

    private static String outputPath(JobConfiguration templateProps) {
        return ofNullable(templateProps.getQsubParameters().getStdout())
                .filter(StringUtils::isNotBlank)
                .map(stdout -> " -o " + stdout)
                .orElse("");
    }

    private static String errorPath(JobConfiguration templateProps) {
        return ofNullable(templateProps.getQsubParameters().getStderr())
                .filter(StringUtils::isNotBlank)
                .map(stderr -> " -e " + stderr)
                .orElse("");
    }

    private static String arguments(JobConfiguration templateProps) {
        List<String> args = templateProps.getQsubParameters().getArguments();
        if(args == null || args.isEmpty()){
            return "";
        }
        return templateProps.getQsubParameters().getArguments().stream()
                .filter(Objects::nonNull)
                .filter(StringUtils::isNotBlank)
                .map(arg -> "\"" + arg + "\"")
                .collect(Collectors.joining(" ", " ", " "));
    }

    private static String script(JobConfiguration templateProps) {
        return ofNullable(templateProps.getQsubParameters().getScript())
                .filter(StringUtils::isNotBlank)
                .map(script -> " " + script)
                .orElseThrow(() -> new IllegalStateException(
                        "Script[" + templateProps.getQsubParameters().getScript() + "] can nut be null or empty"));
    }

}
