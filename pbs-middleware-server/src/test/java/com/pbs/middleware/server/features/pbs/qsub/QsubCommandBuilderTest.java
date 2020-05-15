package com.pbs.middleware.server.features.pbs.qsub;

import com.pbs.middleware.server.features.job.utils.JobConfiguration;
import com.pbs.middleware.common.pbs.QsubParameters;
import com.pbs.middleware.common.pbs.Resources;
import com.pbs.middleware.common.pbs.Walltime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("QSUB command builder tests")
class QsubCommandBuilderTest {

    private static Stream<Arguments> prefixes() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of("", ""),
                Arguments.of(" ", ""),
                Arguments.of("somePrefix", "somePrefix && "),
                Arguments.of("somePrefix ", "somePrefix && "),
                Arguments.of("somePrefix  ", "somePrefix && "),
                Arguments.of("somePrefix &&", "somePrefix && "),
                Arguments.of("somePrefix && ", "somePrefix && "),
                Arguments.of("somePrefix &&  ", "somePrefix && ")
        );
    }

    @ParameterizedTest(name = "Prefix test {index}: {0} -> {1}")
    @DisplayName("Prefix tests")
    @MethodSource("prefixes")
    void prefixTest(String prefix, String expected) {
        QsubParameters qsubParameters = new QsubParameters();
        qsubParameters.setScript("test.sh");

        qsubParameters.setPrefix(prefix);

        JobConfiguration props = new JobConfiguration();
        props.setQsubParameters(qsubParameters);

        QsubCommand cmd = new QsubCommandBuilder().build(props);

        assertEquals(expected, cmd.getPrefix(), "Prefix does not match");
    }

    private static Stream<Arguments> suffixes() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of("", ""),
                Arguments.of(" ", ""),
                Arguments.of("someSuffix", " && someSuffix"),
                Arguments.of(" someSuffix", " && someSuffix"),
                Arguments.of("  someSuffix", " && someSuffix"),
                Arguments.of("&& someSuffix", " && someSuffix"),
                Arguments.of(" && someSuffix", " && someSuffix"),
                Arguments.of("   && someSuffix", " && someSuffix")
        );
    }

    @ParameterizedTest(name = "Suffix test {index}: {0} -> {1}")
    @DisplayName("Suffix tests")
    @MethodSource("suffixes")
    void suffixTest(String suffix, String expected) {
        QsubParameters qsubParameters = new QsubParameters();
        qsubParameters.setScript("test.sh");

        qsubParameters.setSuffix(suffix);

        JobConfiguration props = new JobConfiguration();
        props.setQsubParameters(qsubParameters);

        QsubCommand cmd = new QsubCommandBuilder().build(props);

        assertEquals(expected, cmd.getSuffix(), "Suffix does not match");
    }

    private static Stream<Arguments> workDirectories() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of("", ""),
                Arguments.of(" ", ""),
                Arguments.of("workdir", "cd workdir;")
        );
    }

    @ParameterizedTest(name = "Workdir test {index}: {0} -> {1}")
    @DisplayName("Workdir tests")
    @MethodSource("workDirectories")
    void workDirTest(String workdir, String expected) {
        QsubParameters qsubParameters = new QsubParameters();
        qsubParameters.setScript("test.sh");

        qsubParameters.setWorkdir(workdir);

        JobConfiguration props = new JobConfiguration();
        props.setQsubParameters(qsubParameters);

        QsubCommand cmd = new QsubCommandBuilder().build(props);

        assertEquals(expected, cmd.getWorkDirectory(), "Workdir does not match");
    }

    private static Stream<Arguments> names() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of("", ""),
                Arguments.of(" ", ""),
                Arguments.of("name", " -N name")
        );
    }

    @ParameterizedTest(name = "Job name test {index}: {0} -> {1}")
    @DisplayName("Job name tests")
    @MethodSource("names")
    void nameTest(String name, String expected) {
        QsubParameters qsubParameters = new QsubParameters();
        qsubParameters.setScript("test.sh");

        qsubParameters.setJobName(name);

        JobConfiguration props = new JobConfiguration();
        props.setQsubParameters(qsubParameters);

        QsubCommand cmd = new QsubCommandBuilder().build(props);

        assertEquals(expected, cmd.getName(), "Job name does not match");
    }

    private static Stream<Arguments> variables() {
        return Stream.of(
                Arguments.of((LinkedHashMap<String, String>) null, ""),
                Arguments.of(new LinkedHashMap<>(), ""),
                Arguments.of(new LinkedHashMap<>() {{
                    put(null, null);
                    put(null, null);
                    put(null, "");
                    put(null, " ");
                    put("", null);
                    put(" ", null);
                    put("", "");
                    put(" ", "");
                    put("", " ");
                    put(" ", " ");
                }}, ""),
                Arguments.of(new LinkedHashMap<>() {{
                        put("a", "b");
                        put("c", "d");
                    }}, " a=\"b\",c=\"d\" ")
        );
    }

    @ParameterizedTest(name = "Job variables test {index}: {0} -> {1}")
    @DisplayName("Job variables tests")
    @MethodSource("variables")
    void variablesTest(Map<String, String> vars, String expected) {
        QsubParameters qsubParameters = new QsubParameters();
        qsubParameters.setScript("test.sh");

        qsubParameters.setVariables(vars);

        JobConfiguration props = new JobConfiguration();
        props.setQsubParameters(qsubParameters);

        QsubCommand cmd = new QsubCommandBuilder().build(props);

        assertEquals(expected, cmd.getVars(), "Variables does not match");
    }

    private static Stream<Arguments> queues() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of("", ""),
                Arguments.of(" ", ""),
                Arguments.of("queue", " -q queue")
        );
    }

    @ParameterizedTest(name = "Job queue test {index}: {0} -> {1}")
    @DisplayName("Job queue tests")
    @MethodSource("queues")
    void queueTest(String queue, String expected) {
        QsubParameters qsubParameters = new QsubParameters();
        qsubParameters.setScript("test.sh");

        qsubParameters.setQueue(queue);

        JobConfiguration props = new JobConfiguration();
        props.setQsubParameters(qsubParameters);

        QsubCommand cmd = new QsubCommandBuilder().build(props);

        assertEquals(expected, cmd.getQueue(), "Job queue does not match");
    }

    private static Stream<Arguments> resources() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of(new Resources(), ""),
                Arguments.of(new Resources(Walltime.from("1:0:0"), null, null), " -l walltime=1:0:0")
        );
    }

    @ParameterizedTest(name = "Job resources test {index}: {0} -> {1}")
    @DisplayName("Job resources tests")
    @MethodSource("resources")
    void queueTest(Resources resources, String expected) {
        QsubParameters qsubParameters = new QsubParameters();
        qsubParameters.setScript("test.sh");

        qsubParameters.setResources(resources);

        JobConfiguration props = new JobConfiguration();
        props.setQsubParameters(qsubParameters);

        QsubCommand cmd = new QsubCommandBuilder().build(props);

        assertEquals(expected, cmd.getResources(), "Job resources does not match");
    }

    private static Stream<Arguments> outPaths() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of("", ""),
                Arguments.of(" ", ""),
                Arguments.of("stdoutPath", " -o stdoutPath")
        );
    }

    @ParameterizedTest(name = "STDOUT path test {index}: {0} -> {1}")
    @DisplayName("STDOUT tests")
    @MethodSource("outPaths")
    void outPaths(String stdout, String expected) {
        QsubParameters qsubParameters = new QsubParameters();
        qsubParameters.setScript("test.sh");

        qsubParameters.setStdout(stdout);

        JobConfiguration props = new JobConfiguration();
        props.setQsubParameters(qsubParameters);

        QsubCommand cmd = new QsubCommandBuilder().build(props);

        assertEquals(expected, cmd.getOutputPath(), "Output path does not match");
    }

    private static Stream<Arguments> errPaths() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of("", ""),
                Arguments.of(" ", ""),
                Arguments.of("stderrPath", " -e stderrPath")
        );
    }

    @ParameterizedTest(name = "STDERR path test {index}: {0} -> {1}")
    @DisplayName("STDERR tests")
    @MethodSource("errPaths")
    void errPaths(String stderr, String expected) {
        QsubParameters qsubParameters = new QsubParameters();
        qsubParameters.setScript("test.sh");

        qsubParameters.setStderr(stderr);

        JobConfiguration props = new JobConfiguration();
        props.setQsubParameters(qsubParameters);

        QsubCommand cmd = new QsubCommandBuilder().build(props);

        assertEquals(expected, cmd.getErrorPath(), "Error path does not match");
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of(emptyList(), ""),
                Arguments.of(List.of("a"), " \"a\" "),
                Arguments.of(List.of("a", "b"), " \"a\" \"b\" "),
                Arguments.of(new LinkedList<>(){{
                    add("a");
                    add(null);
                    add("");
                    add("  ");
                    add("b");
                }}, " \"a\" \"b\" ")
        );
    }

    @ParameterizedTest(name = "Argument test {index}: {0} -> {1}")
    @DisplayName("Arguments tests")
    @MethodSource("arguments")
    void arguments(List<String> args, String expected) {
        QsubParameters qsubParameters = new QsubParameters();
        qsubParameters.setScript("test.sh");

        qsubParameters.setArguments(args);

        JobConfiguration props = new JobConfiguration();
        props.setQsubParameters(qsubParameters);

        QsubCommand cmd = new QsubCommandBuilder().build(props);

        assertEquals(expected, cmd.getArguments(), "Arguments does not match");
    }

    private static Stream<Arguments> invalidScripts() {
        return Stream.of(
                Arguments.of(null, "Null script"),
                Arguments.of("", "Empty string"),
                Arguments.of(" ", "Blank string")
        );
    }

    @ParameterizedTest(name = "Invalid scripts test {index}: {1}")
    @DisplayName("Invalid scripts tests")
    @MethodSource("invalidScripts")
    void scripts(String script, String name) {
        QsubParameters qsubParameters = new QsubParameters();
        qsubParameters.setScript(script);

        JobConfiguration props = new JobConfiguration();
        props.setQsubParameters(qsubParameters);

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> new QsubCommandBuilder().build(props)
                , "IllegalStateException exception should be thrown");
    }

    @Test
    @DisplayName("Script test")
    void scriptTest() {
        final String scriptName = "test.sh";
        final String expected = " " + scriptName;

        QsubParameters qsubParameters = new QsubParameters();
        qsubParameters.setScript(scriptName);

        JobConfiguration props = new JobConfiguration();
        props.setQsubParameters(qsubParameters);

        QsubCommand cmd = new QsubCommandBuilder().build(props);

        assertEquals(expected, cmd.getScript(), "Script does not match");
    }

}