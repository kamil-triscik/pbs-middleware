package com.pbs.middleware.server.features.pbs.qsub;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("QSUB command tests")
class QsubCommandTest {

    @Test
    @DisplayName("Qsub command test: full command")
    void fullQsubCommandTest() {
        final String prefix = ". /etc/profile && ";
        final String suffix = " && ls -l";
        final String workDirectory = "cd /home/x;";
        final String name = " -N jobX";
        final String vars = "a=\"b\";c=\"d\"";
        final String queue = " -q q1";
        final String resources = " -l walltime=1:0:0";
        final String outputPath = " -o out.log";
        final String errorPath = " -e err.log";
        final String arguments = " \"x\" \"y\" ";
        final String script = "run.sh";

        final String expectedQSUb = ". /etc/profile && cd /home/x;qsub -N jobX -v a=\"b\";c=\"d\" -q q1 -l walltime=1:0:0 -o out.log -e err.log \"x\" \"y\" run.sh && ls -l";

        QsubCommand cmd = QsubCommand.builder()
                .prefix(prefix)
                .workDirectory(workDirectory)
                .name(name)
                .vars(vars)
                .queue(queue)
                .resources(resources)
                .outputPath(outputPath)
                .errorPath(errorPath)
                .arguments(arguments)
                .script(script)
                .suffix(suffix)
                .build();

        assertEquals(expectedQSUb, cmd.toString(), "QSUB command does not match");
    }

    private static Stream<Arguments> wrongVars() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("  ")
        );
    }

    @ParameterizedTest(name = "Variables = {0}")
    @DisplayName("Qsub command test: empty variables")
    @MethodSource("wrongVars")
    void emptyVariablesQsubCommandTest(String vars) {
        final String prefix = ". /etc/profile && ";
        final String suffix = " && ls -l";
        final String workDirectory = "cd /home/x;";
        final String name = " -N jobX";
        final String queue = " -q q1";
        final String resources = " -l walltime=1:0:0";
        final String outputPath = " -o out.log";
        final String errorPath = " -e err.log";
        final String arguments = " \"x\" \"y\" ";
        final String script = "run.sh";

        final String expectedQSUb = ". /etc/profile && cd /home/x;qsub -N jobX -q q1 -l walltime=1:0:0 -o out.log -e err.log \"x\" \"y\" run.sh && ls -l";

        QsubCommand cmd = QsubCommand.builder()
                .prefix(prefix)
                .workDirectory(workDirectory)
                .name(name)
                .vars(vars)
                .queue(queue)
                .resources(resources)
                .outputPath(outputPath)
                .errorPath(errorPath)
                .arguments(arguments)
                .script(script)
                .suffix(suffix)
                .build();

        assertEquals(expectedQSUb, cmd.toString(), "QSUB command does not match");
    }

    @Test
    @DisplayName("Qsub command test: null variables")
    void nullVariablesQsubCommandTest() {
        final String prefix = ". /etc/profile && ";
        final String suffix = " && ls -l";
        final String workDirectory = "cd /home/x;";
        final String name = " -N jobX";
        final String queue = " -q q1";
        final String resources = " -l walltime=1:0:0";
        final String outputPath = " -o out.log";
        final String errorPath = " -e err.log";
        final String arguments = " \"x\" \"y\" ";
        final String script = "run.sh";

        final String expectedQSUb = ". /etc/profile && cd /home/x;qsub -N jobX -q q1 -l walltime=1:0:0 -o out.log -e err.log \"x\" \"y\" run.sh && ls -l";

        QsubCommand cmd = QsubCommand.builder()
                .prefix(prefix)
                .workDirectory(workDirectory)
                .name(name)
                .vars(null)
                .queue(queue)
                .resources(resources)
                .outputPath(outputPath)
                .errorPath(errorPath)
                .arguments(arguments)
                .script(script)
                .suffix(suffix)
                .build();

        assertEquals(expectedQSUb, cmd.toString(), "QSUB command does not match");
    }

    private static Stream<Arguments> wrongResources() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("  ")
        );
    }

    @ParameterizedTest(name = "Resources = {0}")
    @DisplayName("Qsub command test: empty resources")
    @MethodSource("wrongResources")
    void emptyResourcesQsubCommandTest(String resources) {
        final String prefix = ". /etc/profile && ";
        final String suffix = " && ls -l";
        final String workDirectory = "cd /home/x;";
        final String name = " -N jobX";
        final String vars = "a=\"b\";c=\"d\"";
        final String queue = " -q q1";
        final String outputPath = " -o out.log";
        final String errorPath = " -e err.log";
        final String arguments = " \"x\" \"y\" ";
        final String script = "run.sh";

        final String expectedQSUb = ". /etc/profile && cd /home/x;qsub -N jobX -v a=\"b\";c=\"d\" -q q1 -o out.log -e err.log \"x\" \"y\" run.sh && ls -l";

        QsubCommand cmd = QsubCommand.builder()
                .prefix(prefix)
                .workDirectory(workDirectory)
                .name(name)
                .vars(vars)
                .queue(queue)
                .resources(resources)
                .outputPath(outputPath)
                .errorPath(errorPath)
                .arguments(arguments)
                .script(script)
                .suffix(suffix)
                .build();

        assertEquals(expectedQSUb, cmd.toString(), "QSUB command does not match");
    }

    @Test
    @DisplayName("Qsub command test: null resources")
    void nullResourcesQsubCommandTest() {
        final String prefix = ". /etc/profile && ";
        final String suffix = " && ls -l";
        final String workDirectory = "cd /home/x;";
        final String name = " -N jobX";
        final String vars = "a=\"b\";c=\"d\"";
        final String queue = " -q q1";
        final String outputPath = " -o out.log";
        final String errorPath = " -e err.log";
        final String arguments = " \"x\" \"y\" ";
        final String script = "run.sh";

        final String expectedQSUb = ". /etc/profile && cd /home/x;qsub -N jobX -v a=\"b\";c=\"d\" -q q1 -o out.log -e err.log \"x\" \"y\" run.sh && ls -l";

        QsubCommand cmd = QsubCommand.builder()
                .prefix(prefix)
                .workDirectory(workDirectory)
                .name(name)
                .vars(vars)
                .queue(queue)
                .resources(null)
                .outputPath(outputPath)
                .errorPath(errorPath)
                .arguments(arguments)
                .script(script)
                .suffix(suffix)
                .build();

        assertEquals(expectedQSUb, cmd.toString(), "QSUB command does not match");
    }

}