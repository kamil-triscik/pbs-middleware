package com.pbs.middleware.server.features.ssh.shell;

import java.util.stream.Collectors;
import lombok.Getter;

import static java.util.stream.Stream.of;

@Getter
public class Result {

    private final Integer exitCode;

    private final String stdout;

    private final String stderr;

    public Result(Integer exitCode, String stdout, String stderr) {
        this.exitCode = exitCode != null ? exitCode : -1;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    @Override
    public String toString() {
        return "\nResult: {" +
                "\n\tExitCode: " + (stdout.isBlank() ? "<no output>" : of(stdout.split("\n")).map(line -> "\n\t\t>>> " + line).collect(Collectors.joining())) +
                "\n\tSTDOUT: " + (stdout.isBlank() ? "<no output>" : of(stdout.split("\n")).map(line -> "\n\t\t>>> " + line).collect(Collectors.joining())) +
                "\n\tSTDERR: " + (stderr.isBlank() ? "<no output>" : of(stderr.split("\n")).map(line -> "\n\t\t>>> " + line).collect(Collectors.joining())) +
                "\n}";
    }
}
