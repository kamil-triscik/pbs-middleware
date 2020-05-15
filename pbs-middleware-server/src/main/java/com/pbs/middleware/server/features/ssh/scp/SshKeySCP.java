package com.pbs.middleware.server.features.ssh.scp;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.pbs.middleware.server.features.connection.repository.SshKeyConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class SshKeySCP extends JschSCP {

    private final String sshKey;

    SshKeySCP(SshKeyConnection connection) {
        super(connection.getLogin(), connection.getSshHost());
        this.sshKey = connection.getSshKey();
    }

    @Override
    protected void initSshSession() throws SCPException {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(sshKey);
            createSession(jsch);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
        } catch (JSchException exception) {
            throw new SCPException("SCP session exception", exception);
        }
    }
}
