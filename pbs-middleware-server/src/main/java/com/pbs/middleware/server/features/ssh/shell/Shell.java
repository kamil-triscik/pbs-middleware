package com.pbs.middleware.server.features.ssh.shell;


import java.io.InputStream;
import java.io.OutputStream;

public interface Shell {

    Result executeCommand(String command) throws ShellException;

    InputStream getInputStream() throws ShellException;

    OutputStream getOutputStream() throws ShellException;

}
