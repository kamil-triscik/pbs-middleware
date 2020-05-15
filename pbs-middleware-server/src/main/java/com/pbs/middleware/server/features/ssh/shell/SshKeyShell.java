package com.pbs.middleware.server.features.ssh.shell;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.pbs.middleware.server.features.connection.repository.SshKeyConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class SshKeyShell extends JschShell {

    private final String sshKey;

    SshKeyShell(SshKeyConnection connection) {
        super(connection.getPbsHost(), connection.getSshHost(), connection.getLogin());
        this.sshKey = connection.getSshKey();
    }

    @Override
    protected void initSshSession() throws ShellException {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(sshKey);
            createSession(jsch);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
        } catch (JSchException exception) {
            throw new ShellException("SSH session exception: " + exception.getLocalizedMessage());
        }
    }
}
