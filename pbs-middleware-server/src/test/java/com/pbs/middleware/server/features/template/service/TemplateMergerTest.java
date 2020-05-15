package com.pbs.middleware.server.features.template.service;

import com.pbs.middleware.common.pbs.Walltime;
import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import com.pbs.middleware.common.pbs.QsubParameters;
import com.pbs.middleware.common.pbs.Resources;
import com.pbs.middleware.common.pbs.Walltime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.pbs.middleware.server.features.job.utils.merge.JobConfigurationMerger.merge;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Template merger tests")
class TemplateMergerTest {

    @Test
    @DisplayName("Changes template props are null")
    void nothingToMergeTest() {

        JobConfiguration orig = new JobConfiguration();

        JobConfiguration merged = merge(orig, null);

        assertSame(orig, merged);
    }

    @Test
    @DisplayName("Merger.group: merge value")
    void groupMergeTest() {
        final String newValue = "new";

        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setGroup(newValue);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig, merged);
        assertEquals(newValue, merged.getGroup());
    }

    @Test
    @DisplayName("Merger.group: merge null value")
    void nullGroupMergeTest() {
        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setGroup(null);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig, merged);
        assertEquals(orig.getGroup(), merged.getGroup());
    }

    @Test
    @DisplayName("Merger.group: merge empty value")
    void emptyGroupMergeTest() {
        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setGroup("");

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig, merged);
        assertEquals(orig.getGroup(), merged.getGroup());
    }

    @Test
    @DisplayName("Merger.group: merge blank value")
    void blankGroupMergeTest() {
        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setGroup(" ");

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig, merged);
        assertEquals(orig.getGroup(), merged.getGroup());
    }

    @Test
    @DisplayName("Merger.connection: merge value")
    void connectionMergeTest() {
        final String newValue = "new";

        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setConnection(newValue);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig, merged);
        assertEquals(newValue, merged.getConnection());
    }

    @Test
    @DisplayName("Merger.connection: merge null value")
    void nullConnectionMergeTest() {
        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setConnection(null);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig, merged);
        assertEquals(orig.getConnection(), merged.getConnection());
    }

    @Test
    @DisplayName("Merger.connection: merge empty value")
    void emptyConnectionMergeTest() {
        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setConnection("");

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig, merged);
        assertEquals(orig.getConnection(), merged.getConnection());
    }

    @Test
    @DisplayName("Merger.connection: merge blank value")
    void blankConnectionMergeTest() {
        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setConnection(" ");

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig, merged);
        assertEquals(orig.getConnection(), merged.getConnection());
    }

    @Test
    @DisplayName("Merger.script: merge value")
    void scriptMergeTest() {
        final String newValue = "new";

        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setScript(newValue);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig, merged);
        assertEquals(newValue, merged.getScript());
    }

    @Test
    @DisplayName("Merger.script: merge null value")
    void nullScriptMergeTest() {
        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setScript(null);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig, merged);
        assertEquals(orig.getScript(), merged.getScript());
    }

    @Test
    @DisplayName("Merger.script: merge empty value")
    void emptyScriptMergeTest() {
        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setScript("");

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig, merged);
        assertEquals(orig.getScript(), merged.getScript());
    }

    @Test
    @DisplayName("Merger.script: merge blank value")
    void blankScriptMergeTest() {
        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setScript(" ");

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig, merged);
        assertEquals(orig.getScript(), merged.getScript());
    }

    @Test
    @DisplayName("Merger.QsubParams: changes are null")
    void nullQsubParamsTest() {
        JobConfiguration orig = getOriginalProps();

        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(null);

        JobConfiguration merged = merge(orig, changed);

        assertSame(orig.getQsubParameters(), merged.getQsubParameters());
    }

    @Test
    @DisplayName("Merger.QsubParams.variables: changes are null")
    void nullVariablesTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setVariables(null);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getVariables(), merged.getQsubParameters().getVariables());
    }

    @Test
    @DisplayName("Merger.QsubParams.variables: origin params map is null")
    void nullOriginVariablesTest() {
        JobConfiguration orig = getOriginalProps();
        orig.getQsubParameters().setVariables(null);

        QsubParameters params = new QsubParameters();
        params.setVariables(Map.of("x", "y"));
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertNotSame(changed.getQsubParameters().getVariables(), merged.getQsubParameters().getVariables());
        assertEquals(changed.getQsubParameters().getVariables(), merged.getQsubParameters().getVariables());
    }

    @Test
    @DisplayName("Merger.QsubParams.variables: merge")
    void variablesMergeTest() {
        Map<String, String> origVars = Map.of("a", "b", "c", "d", "del", "toDel");
        Map<String, String> changeVars = new HashMap<>() {{
            put("c", "dd");
            put("e", "f");
            put("del", null);
        }};

        JobConfiguration orig = getOriginalProps();
        orig.getQsubParameters().setVariables(origVars);

        QsubParameters params = new QsubParameters();
        params.setVariables(changeVars);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertNotSame(orig.getQsubParameters().getVariables(), merged.getQsubParameters().getVariables());
        assertNotSame(changed.getQsubParameters().getVariables(), merged.getQsubParameters().getVariables());
        assertEquals(3, merged.getQsubParameters().getVariables().size());
        assertEquals("b", merged.getQsubParameters().getVariables().get("a"));
        assertEquals("dd", merged.getQsubParameters().getVariables().get("c"));
        assertEquals("f", merged.getQsubParameters().getVariables().get("e"));
        assertNull(merged.getQsubParameters().getVariables().get("del"));
    }

    @Test
    @DisplayName("Merger.QsubParams.stdout: changes are null")
    void nullStdoutMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setStdout(null);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getStdout(), merged.getQsubParameters().getStdout());
    }

    @Test
    @DisplayName("Merger.QsubParams.stdout: changes are empty")
    void emptyStdoutMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setStdout("");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getStdout(), merged.getQsubParameters().getStdout());
    }

    @Test
    @DisplayName("Merger.QsubParams.stdout: changes are blank")
    void blankStdoutMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setStdout(" ");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getStdout(), merged.getQsubParameters().getStdout());
    }

    @Test
    @DisplayName("Merger.QsubParams.stdout: values merge")
    void stdoutMergeTest() {
        final String newValue = "newValue";

        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setStdout(newValue);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(newValue, merged.getQsubParameters().getStdout());
    }

    @Test
    @DisplayName("Merger.QsubParams.stderr: changes are null")
    void nullStderrMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setStderr(null);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getStderr(), merged.getQsubParameters().getStderr());
    }

    @Test
    @DisplayName("Merger.QsubParams.stderr: changes are empty")
    void emptyStderrMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setStderr("");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getStderr(), merged.getQsubParameters().getStderr());
    }

    @Test
    @DisplayName("Merger.QsubParams.stderr: changes are blank")
    void blankStderrMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setStderr(" ");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getStderr(), merged.getQsubParameters().getStderr());
    }

    @Test
    @DisplayName("Merger.QsubParams.stderr: values merge")
    void stderrMergeTest() {
        final String newValue = "newValue";

        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setStderr(newValue);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(newValue, merged.getQsubParameters().getStderr());
    }

    @Test
    @DisplayName("Merger.QsubParams.prefix: changes are null")
    void nullPrefixMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setPrefix(null);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getPrefix(), merged.getQsubParameters().getPrefix());
    }

    @Test
    @DisplayName("Merger.QsubParams.prefix: changes are empty")
    void emptyPrefixMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setPrefix("");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getPrefix(), merged.getQsubParameters().getPrefix());
    }

    @Test
    @DisplayName("Merger.QsubParams.prefix: changes are blank")
    void blankPrefixMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setPrefix(" ");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getPrefix(), merged.getQsubParameters().getPrefix());
    }

    @Test
    @DisplayName("Merger.QsubParams.prefix: values merge")
    void prefixMergeTest() {
        final String newValue = "newValue";

        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setPrefix(newValue);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(newValue, merged.getQsubParameters().getPrefix());
    }

    @Test
    @DisplayName("Merger.QsubParams.script: changes are null")
    void nullQsubScriptMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setScript(null);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getScript(), merged.getQsubParameters().getScript());
    }

    @Test
    @DisplayName("Merger.QsubParams.script: changes are empty")
    void emptyQsubScriptMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setScript("");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getScript(), merged.getQsubParameters().getScript());
    }

    @Test
    @DisplayName("Merger.QsubParams.script: changes are blank")
    void blankQsubScriptMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setScript(" ");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getScript(), merged.getQsubParameters().getScript());
    }

    @Test
    @DisplayName("Merger.QsubParams.script: values merge")
    void qsubScriptMergeTest() {
        final String newValue = "newValue";

        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setScript(newValue);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(newValue, merged.getQsubParameters().getScript());
    }

    @Test
    @DisplayName("Merger.QsubParams.suffix: changes are null")
    void nullSuffixMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setSuffix(null);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getSuffix(), merged.getQsubParameters().getSuffix());
    }

    @Test
    @DisplayName("Merger.QsubParams.suffix: changes are empty")
    void emptynullSuffixMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setSuffix("");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getSuffix(), merged.getQsubParameters().getSuffix());
    }

    @Test
    @DisplayName("Merger.QsubParams.suffix: changes are blank")
    void blankSuffixMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setSuffix(" ");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getSuffix(), merged.getQsubParameters().getSuffix());
    }

    @Test
    @DisplayName("Merger.QsubParams.suffix: values merge")
    void suffixMergeTest() {
        final String newValue = "newValue";

        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setSuffix(newValue);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(newValue, merged.getQsubParameters().getSuffix());
    }

    @Test
    @DisplayName("Merger.QsubParams.queue: changes are null")
    void nullQueueMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setQueue(null);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getQueue(), merged.getQsubParameters().getQueue());
    }

    @Test
    @DisplayName("Merger.QsubParams.queue: changes are empty")
    void emptyQueueMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setQueue("");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getQueue(), merged.getQsubParameters().getQueue());
    }

    @Test
    @DisplayName("Merger.QsubParams.queue: changes are blank")
    void blankQueueMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setQueue(" ");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getQueue(), merged.getQsubParameters().getQueue());
    }

    @Test
    @DisplayName("Merger.QsubParams.queue: values merge")
    void queueMergeTest() {
        final String newValue = "newValue";

        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setQueue(newValue);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(newValue, merged.getQsubParameters().getQueue());
    }

    @Test
    @DisplayName("Merger.QsubParams.jobName: changes are null")
    void nullJobNameMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setJobName(null);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getJobName(), merged.getQsubParameters().getJobName());
    }

    @Test
    @DisplayName("Merger.QsubParams.jobName: changes are empty")
    void emptyJobNameMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setJobName("");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getJobName(), merged.getQsubParameters().getJobName());
    }

    @Test
    @DisplayName("Merger.QsubParams.jobName: changes are blank")
    void blankJobNameMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setJobName(" ");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getJobName(), merged.getQsubParameters().getJobName());
    }

    @Test
    @DisplayName("Merger.QsubParams.jobName: values merge")
    void jobNameMergeTest() {
        final String newValue = "newValue";

        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setJobName(newValue);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(newValue, merged.getQsubParameters().getJobName());
    }

    @Test
    @DisplayName("Merger.QsubParams.workdir: changes are null")
    void nullWorkdirMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setWorkdir(null);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getWorkdir(), merged.getQsubParameters().getWorkdir());
    }

    @Test
    @DisplayName("Merger.QsubParams.workdir: changes are empty")
    void emptyWorkdirMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setWorkdir("");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getWorkdir(), merged.getQsubParameters().getWorkdir());
    }

    @Test
    @DisplayName("Merger.QsubParams.workdir: changes are blank")
    void blankWorkdirMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setWorkdir(" ");
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getWorkdir(), merged.getQsubParameters().getWorkdir());
    }

    @Test
    @DisplayName("Merger.QsubParams.workdir: values merge")
    void workdirMergeTest() {
        final String newValue = "newValue";

        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setWorkdir(newValue);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(newValue, merged.getQsubParameters().getWorkdir());
    }

    @Test
    @DisplayName("Merger.QsubParams.arguments: changes are null")
    void nullArgumentsMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setArguments(null);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertEquals(orig.getQsubParameters().getArguments(), merged.getQsubParameters().getArguments());
    }

    @Test
    @DisplayName("Merger.QsubParams.arguments: values merge")
    void argumentsMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setArguments(List.of("newVal"));
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertNotSame(orig.getQsubParameters().getArguments(), merged.getQsubParameters().getArguments());
        assertNotSame(changed.getQsubParameters().getArguments(), merged.getQsubParameters().getArguments());
        assertEquals(
                orig.getQsubParameters().getArguments().size() + changed.getQsubParameters().getArguments().size(),
                merged.getQsubParameters().getArguments().size());
        assertTrue(merged.getQsubParameters().getArguments().containsAll(orig.getQsubParameters().getArguments()));
        assertTrue(merged.getQsubParameters().getArguments().containsAll(changed.getQsubParameters().getArguments()));
    }

    @Test
    @DisplayName("Merger.QsubParams.arguments: orig is null")
    void origArgumentsAreNullMergeTest() {
        JobConfiguration orig = getOriginalProps();
        orig.getQsubParameters().setArguments(null);

        QsubParameters params = new QsubParameters();
        params.setArguments(List.of("newVal"));
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertNotSame(orig.getQsubParameters().getArguments(), merged.getQsubParameters().getArguments());
        assertNotSame(changed.getQsubParameters().getArguments(), merged.getQsubParameters().getArguments());
        assertEquals(changed.getQsubParameters().getArguments().size(), merged.getQsubParameters().getArguments().size());
        assertTrue(merged.getQsubParameters().getArguments().containsAll(changed.getQsubParameters().getArguments()));
    }

    @Test
    @DisplayName("Merger.QsubParams.arguments: invalid values merge")
    void invalidArgumentsMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setArguments(new ArrayList<>() {{
            add(" ");
            add("  ");
            add(null);
        }});
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertNotSame(orig.getQsubParameters().getArguments(), merged.getQsubParameters().getArguments());
        assertNotSame(changed.getQsubParameters().getArguments(), merged.getQsubParameters().getArguments());
        assertEquals(orig.getQsubParameters().getArguments().size(), merged.getQsubParameters().getArguments().size());
        assertTrue(merged.getQsubParameters().getArguments().containsAll(orig.getQsubParameters().getArguments()));
        assertFalse(merged.getQsubParameters().getArguments().containsAll(changed.getQsubParameters().getArguments()));
    }

    @Test
    @DisplayName("Merger.QsubParams.resource: orig is null")
    void nullOrigResourceMergeTest() {
        JobConfiguration orig = getOriginalProps();
        orig.getQsubParameters().setResources(null);

        QsubParameters params = new QsubParameters();
        params.setResources(getOriginalResources());
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(changed.getQsubParameters().getResources(), merged.getQsubParameters().getResources());
        assertEquals(changed.getQsubParameters().getResources(), merged.getQsubParameters().getResources());
    }

    @Test
    @DisplayName("Merger.QsubParams.resource: orig & change are null")
    void nullOrigAndChangeResourceMergeTest() {
        JobConfiguration orig = getOriginalProps();
        orig.getQsubParameters().setResources(null);

        QsubParameters params = new QsubParameters();
        params.setResources(null);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNull(merged.getQsubParameters().getResources());
    }

    @Test
    @DisplayName("Merger.QsubParams.resource: change is null")
    void nullChangeResourceMergeTest() {
        JobConfiguration orig = getOriginalProps();

        QsubParameters params = new QsubParameters();
        params.setResources(null);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters().getResources(), merged.getQsubParameters().getResources());
        assertEquals(orig.getQsubParameters().getResources(), merged.getQsubParameters().getResources());
    }

    @Test
    @DisplayName("Merger.QsubParams.resources.w: values merge")
    void wMergeTest() {
        final Walltime newValue = Walltime.from("2:0:0");

        JobConfiguration orig = getOriginalProps();

        Resources resources = new Resources();
        resources.setWalltime(newValue);
        QsubParameters params = new QsubParameters();
        params.setResources(resources);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters().getResources().getWalltime(), merged.getQsubParameters().getResources().getWalltime());
        assertEquals(newValue, merged.getQsubParameters().getResources().getWalltime());
    }

    @Test
    @DisplayName("Merger.QsubParams.resources.w: changes are null")
    void nullWMergeTest() {
        JobConfiguration orig = getOriginalProps();

        Resources resources = new Resources();
        resources.setWalltime(null);
        QsubParameters params = new QsubParameters();
        params.setResources(resources);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters().getResources().getWalltime(), merged.getQsubParameters().getResources().getWalltime());
        assertEquals(orig.getQsubParameters().getResources().getWalltime(), merged.getQsubParameters().getResources().getWalltime());
    }

    @Test
    @DisplayName("Merger.QsubParams.resources.customResources: changes are null")
    void nullCustomResourcesTest() {
        JobConfiguration orig = getOriginalProps();

        Resources resources = new Resources();
        resources.setCustomResources(null);
        QsubParameters params = new QsubParameters();
        params.setResources(resources);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertNotSame(orig.getQsubParameters().getResources(), merged.getQsubParameters().getResources());
        assertNotSame(orig.getQsubParameters().getResources().getCustomResources(), merged.getQsubParameters().getResources().getCustomResources());
        assertEquals(orig.getQsubParameters().getResources().getCustomResources(), merged.getQsubParameters().getResources().getCustomResources());
    }

    @Test
    @DisplayName("Merger.QsubParams.resources.customResources: origin params map is null")
    void nullOriginCustomResourcesTest() {
        JobConfiguration orig = getOriginalProps();
        orig.getQsubParameters().getResources().setCustomResources(null);

        Resources resources = new Resources();
        resources.setCustomResources(Map.of("x", "y"));
        QsubParameters params = new QsubParameters();
        params.setResources(resources);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertNotSame(orig.getQsubParameters().getResources(), merged.getQsubParameters().getResources());
        assertNotSame(changed.getQsubParameters().getResources().getCustomResources(), merged.getQsubParameters().getResources().getCustomResources());
        assertEquals(changed.getQsubParameters().getResources().getCustomResources(), merged.getQsubParameters().getResources().getCustomResources());
    }

    @Test
    @DisplayName("Merger.QsubParams.resources.customResources:: merge")
    void customResourcesMergeTest() {
        Map<String, String> origCP = Map.of("a", "b", "c", "d", "del", "toDel", "", "empty", "empty2", "", "blank", " ", " ", "blank2");
        Map<String, String> changeCP = new HashMap<>() {{
            put("c", "dd");
            put("e", "f");
            put("del", null);
            put("  ", "blank");
            put("empty3", "");
            put("blank3", "    ");
        }};

        JobConfiguration orig = getOriginalProps();
        orig.getQsubParameters().getResources().setCustomResources(origCP);

        Resources resources = new Resources();
        resources.setCustomResources(changeCP);
        QsubParameters params = new QsubParameters();
        params.setResources(resources);
        JobConfiguration changed = new JobConfiguration();
        changed.setQsubParameters(params);

        JobConfiguration merged = merge(orig, changed);

        assertNotSame(orig.getQsubParameters(), merged.getQsubParameters());
        assertNotSame(orig.getQsubParameters().getResources(), merged.getQsubParameters().getResources());
        assertNotSame(changed.getQsubParameters().getResources().getCustomResources(), merged.getQsubParameters().getResources().getCustomResources());

        assertEquals(3, merged.getQsubParameters().getResources().getCustomResources().size());
        assertEquals("b", merged.getQsubParameters().getResources().getCustomResources().get("a"));
        assertEquals("dd", merged.getQsubParameters().getResources().getCustomResources().get("c"));
        assertEquals("f", merged.getQsubParameters().getResources().getCustomResources().get("e"));
        assertNull(merged.getQsubParameters().getResources().getCustomResources().get("del"));
        assertNull(merged.getQsubParameters().getResources().getCustomResources().get("empty2"));
        assertNull(merged.getQsubParameters().getResources().getCustomResources().get("blank"));
        assertNull(merged.getQsubParameters().getResources().getCustomResources().get(""));
        assertNull(merged.getQsubParameters().getResources().getCustomResources().get(" "));
        assertNull(merged.getQsubParameters().getResources().getCustomResources().get("empty3"));
        assertNull(merged.getQsubParameters().getResources().getCustomResources().get("blank3"));
        assertNull(merged.getQsubParameters().getResources().getCustomResources().get("  "));
    }

    // todo chunks

    private JobConfiguration getOriginalProps() {
        JobConfiguration orig = new JobConfiguration();

        orig.setGroup("group");
        orig.setConnection("connection");
        orig.setScript("script");
        orig.setQsubParameters(getOriginalQsubParams());

        return orig;
    }

    private QsubParameters getOriginalQsubParams() {
        QsubParameters orig = new QsubParameters();

        orig.setResources(getOriginalResources());
        orig.setVariables(Map.of("key1", "val1"));
        orig.setStdout("stdout");
        orig.setStderr("stderr");
        orig.setPrefix("prefix");
        orig.setScript("script");
        orig.setSuffix("suffix");
        orig.setQueue("queue");
        orig.setJobName("jobX");
        orig.setWorkdir("/home/X");
        orig.setArguments(List.of("arg1", "arg2"));

        return orig;
    }

    private Resources getOriginalResources() {
        Resources orig = new Resources();

        orig.setWalltime(Walltime.from("1:0:0"));
        orig.setCustomResources(new LinkedHashMap<>() {{ put("a", "b");}});
        // todo chunks

        return orig;
    }

}