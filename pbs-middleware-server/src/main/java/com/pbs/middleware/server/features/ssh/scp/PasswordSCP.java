package com.pbs.middleware.server.features.ssh.scp;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.pbs.middleware.server.features.connection.repository.PasswordConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class PasswordSCP extends JschSCP {

    private final String password;

    PasswordSCP(PasswordConnection connection) {
        super(connection.getLogin(), connection.getSshHost());
        this.password = connection.getPassword();
    }

    @Override
    protected void initSshSession() throws SCPException {
        try {
            createSession(new JSch());
            session.setPassword(this.password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
        } catch (JSchException exception) {
            throw new SCPException("SSH session exception", exception);
        }
    }
}
