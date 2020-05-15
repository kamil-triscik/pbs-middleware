package com.pbs.middleware.server.features.ssh.shell;

import com.pbs.middleware.server.features.connection.repository.Connection;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;
import org.apache.commons.io.IOUtils;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

public class LocalShell implements Shell, CloseableShell {

    private final String pbsHost;

    private Process process;

    LocalShell(Connection connection) {
        this.pbsHost = connection.getPbsHost();
    }

    @Override
    public Result executeCommand(String command) throws ShellException {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();

            processBuilder.command(asList(command.trim().replaceAll(" +", " ").split(" ")));

            Map<String, String> env = processBuilder.environment();
            env.put("PBS_HOST", this.pbsHost);

            process = processBuilder.start();
            final int exitCode = process.waitFor();
            return new Result(exitCode, IOUtils.toString(new InputStreamReader(process.getInputStream())), IOUtils.toString(new InputStreamReader(process.getErrorStream())));
        } catch (Exception e) {
            throw new ShellException("Local shell error: " + e.getLocalizedMessage());
        }
    }

    @Override
    public InputStream getInputStream() throws ShellException {
        return ofNullable(process).map(Process::getInputStream).orElse(null);
    }

    @Override
    public OutputStream getOutputStream() throws ShellException {
        return ofNullable(process).map(Process::getOutputStream).orElse(null);
    }

    @Override
    public void close() throws IOException {
        ofNullable(process).filter(Process::isAlive).ifPresent(Process::destroy);
    }
}
