package com.pbs.middleware.server.features.ssh.shell;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.pbs.middleware.server.features.connection.repository.PasswordConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class PasswordShell extends JschShell {

    private final String password;

    PasswordShell(PasswordConnection connection) {
        super(connection.getPbsHost(), connection.getLogin(), connection.getSshHost());
        this.password = connection.getPassword();
    }

    @Override
    protected void initSshSession() throws ShellException {
        try {
            createSession(new JSch());
            session.setPassword(this.password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
        } catch (JSchException exception) {
            throw new ShellException("SSH session exception: " + exception.getLocalizedMessage());
        }
    }
}
