package com.pbs.middleware.server.features.ssh.shell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ShellDecorator implements Shell {

    private final CloseableShell shell;

    @Override
    public Result executeCommand(String command) throws ShellException {
        try(shell) {
            return this.shell.executeCommand(command);
        } catch (IOException exception) {
            throw new ShellException("Shell IO exception: " + exception.getLocalizedMessage());
        }
    }

    @Override
    public InputStream getInputStream() throws ShellException {
        return shell.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws ShellException {
        return shell.getOutputStream();
    }
}
